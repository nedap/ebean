package org.tests.o2m.dm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.annotation.SoftDelete;
import org.tests.model.draftable.BaseDomain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkflowOperationEntity extends HistoryColumns {
  private String name;

  @ManyToOne
  @JoinColumn(name = "workflow_id")
  @JsonIgnore
  private WorkflowEntity workflowEntity;
  @SoftDelete
  private boolean deleted;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public WorkflowEntity getWorkflowEntity() {
    return workflowEntity;
  }

  public void setWorkflowEntity(WorkflowEntity workflowEntity) {
    this.workflowEntity = workflowEntity;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}