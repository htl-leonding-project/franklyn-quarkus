package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

//@Entity
//@Table(name = "F_IMAGE")
public class Image extends PanacheEntityBase {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "I_ID", nullable = false)
    private Long id;

    //@Column(name = "I_BYTES")
    private byte[] imgInByte;

    private String imageFile;

    //region Constructor
    public Image() {
    }

    public Image(byte[] imgInByte) {
        this.imgInByte = imgInByte;
    }
    //endregion Constructor
}
