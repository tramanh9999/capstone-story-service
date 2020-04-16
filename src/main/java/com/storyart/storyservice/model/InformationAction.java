package com.storyart.storyservice.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "information_action")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(InformationActionId.class)
public class InformationAction {
    @Id
    @NotNull
    private String actionId;

    @Id
    @NotNull
    private String informationId;

    private String value;

    @NotBlank
    private String operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationAction info = (InformationAction) o;
        return actionId == info.getActionId() && informationId == info.getInformationId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionId, informationId);
    }
}

@Setter
@Getter
@NoArgsConstructor
class InformationActionId implements Serializable{
    private String actionId;
    private String informationId;
}
