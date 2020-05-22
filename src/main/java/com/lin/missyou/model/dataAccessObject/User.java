package com.lin.missyou.model.dataAccessObject;

import com.lin.missyou.util.MapAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String openid;
    private String nickname;
    private Long unifyUid;
    private String email;
    private String password;
    private String mobile;
//    private String group;
    @Convert(converter = MapAndJson.class)
    private Map<String,Object> wxProfile;
}
