package com.kavala.inventory_service.infrastructure.adapter.persistence;

import com.kavala.inventory_service.domain.model.*;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain BookCopy and JPA entity.
 * Follows Hexagonal Architecture by keeping domain model clean from persistence
 * concerns.
 */
@Component
public class BookCopyMapper {

    /**
     * Maps JPA entity to domain aggregate.
     */
    public BookCopy toDomain(JpaBookCopyEntity entity) {
        if (entity == null) {
            return null;
        }

        ShelfLocation shelfLocation = null;
        if (entity.hasShelfLocation()) {
            shelfLocation = entity.getPosition() != null
                    ? ShelfLocation.of(entity.getFloor(), entity.getSection(), entity.getShelf(), entity.getPosition())
                    : ShelfLocation.of(entity.getFloor(), entity.getSection(), entity.getShelf());
        }

        AuditInfo auditInfo = AuditInfo.of(
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getAcquiredAt());

        return BookCopy.reconstitute(
                BookCopyId.of(entity.getId()),
                BookId.of(entity.getBookId()),
                Barcode.of(entity.getBarcode()),
                shelfLocation,
                toDomainStatus(entity.getStatus()),
                auditInfo);
    }

    /**
     * Maps domain aggregate to JPA entity.
     */
    public JpaBookCopyEntity toEntity(BookCopy domain) {
        if (domain == null) {
            return null;
        }

        JpaBookCopyEntity entity = new JpaBookCopyEntity();
        entity.setId(domain.getId().getValue());
        entity.setBookId(domain.getBookId().getValue());
        entity.setBarcode(domain.getBarcode().getValue());
        entity.setStatus(toEntityStatus(domain.getStatus()));

        domain.getShelfLocation().ifPresent(location -> {
            entity.setFloor(location.getFloor());
            entity.setSection(location.getSection());
            entity.setShelf(location.getShelf());
            location.getPosition().ifPresent(entity::setPosition);
        });

        entity.setCreatedAt(domain.getAuditInfo().getCreatedAt());
        entity.setUpdatedAt(domain.getAuditInfo().getUpdatedAt().orElse(null));
        entity.setAcquiredAt(domain.getAuditInfo().getAcquiredAt().orElse(null));

        return entity;
    }

    /**
     * Updates existing entity from domain aggregate.
     */
    public void updateEntity(JpaBookCopyEntity entity, BookCopy domain) {
        entity.setBarcode(domain.getBarcode().getValue());
        entity.setStatus(toEntityStatus(domain.getStatus()));

        // Handle shelf location
        if (domain.getShelfLocation().isPresent()) {
            ShelfLocation location = domain.getShelfLocation().get();
            entity.setFloor(location.getFloor());
            entity.setSection(location.getSection());
            entity.setShelf(location.getShelf());
            entity.setPosition(location.getPosition().orElse(null));
        } else {
            entity.setFloor(null);
            entity.setSection(null);
            entity.setShelf(null);
            entity.setPosition(null);
        }

        entity.setUpdatedAt(domain.getAuditInfo().getUpdatedAt().orElse(null));
    }

    private CopyStatus toDomainStatus(JpaBookCopyEntity.CopyStatusEntity entityStatus) {
        return switch (entityStatus) {
            case AVAILABLE -> CopyStatus.AVAILABLE;
            case LOANED -> CopyStatus.LOANED;
            case RESERVED -> CopyStatus.RESERVED;
            case LOST -> CopyStatus.LOST;
            case DAMAGED -> CopyStatus.DAMAGED;
            case WITHDRAWN -> CopyStatus.WITHDRAWN;
        };
    }

    private JpaBookCopyEntity.CopyStatusEntity toEntityStatus(CopyStatus domainStatus) {
        return switch (domainStatus) {
            case AVAILABLE -> JpaBookCopyEntity.CopyStatusEntity.AVAILABLE;
            case LOANED -> JpaBookCopyEntity.CopyStatusEntity.LOANED;
            case RESERVED -> JpaBookCopyEntity.CopyStatusEntity.RESERVED;
            case LOST -> JpaBookCopyEntity.CopyStatusEntity.LOST;
            case DAMAGED -> JpaBookCopyEntity.CopyStatusEntity.DAMAGED;
            case WITHDRAWN -> JpaBookCopyEntity.CopyStatusEntity.WITHDRAWN;
        };
    }
}
