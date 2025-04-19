package com.assessmint.be.global;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

   @CreatedDate
   @Temporal(TemporalType.TIMESTAMP)
   private Date createdOn;

   @CreatedBy
   @Column
   private String createdBy;

   @LastModifiedDate
   @Temporal(TemporalType.TIMESTAMP)
   private Date updatedOn;

   @LastModifiedBy
   @Column
   private String updatedBy;

   @Temporal(TemporalType.TIMESTAMP)
   private Date deletedOn;

   @Column
   private String deletedBy;

   @Column
   private Boolean isDeleted = false;
}
