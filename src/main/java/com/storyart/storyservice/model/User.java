package com.storyart.storyservice.model;

import com.storyart.storyservice.common.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email") })
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên đăng nhập không được trống")
    @Size(min=3,max = 15, message = "Tên đăng nhập phải có từ 3 đến 15 ký tự")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 40, min = 3, message = "Tên phải có từ 3 đến 40 ký tự")
    @Column(length = 40)
    private String name;

    @NotBlank(message = "Mật khẩu không được để trống")
    //size 100 is encoded password,, signup request has passord <=15
    @Size(max = 100, min = 8, message = "Mật khẩu phải có từ 8 đến 100 ký tự")
    private String password;

    @Size(max = 1000, message = "")
    private String avatar;

    private int roleId;

    @Size(max = 300, message = "Thông tin giới thiệu có độ dài tối đa là 300 ký tự")
    @Column(length = 300)
    private String introContent;
    private boolean isActive;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    private Date createdAt;

    private Date updatedAt;

    boolean isDeactiveByAdmin;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
