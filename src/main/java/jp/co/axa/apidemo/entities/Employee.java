package jp.co.axa.apidemo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_NAME")
    @JsonProperty("name")
    private String name;

    @Getter
    @Setter
    @Column(name="EMPLOYEE_SALARY")
    @JsonProperty("salary")
    private Integer salary;

    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    @JsonProperty("department")
    private String department;

}
