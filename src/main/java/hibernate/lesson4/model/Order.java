package hibernate.lesson4.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ORDERS")
public class Order {
    private Long id;
    private User user;
    private Room room;
    private Date dateFrom;
    private Date dateTo;
    private Double moneyPaid;

    public Order() {
    }

    public Order(User user, Room room, Date dateFrom, Date dateTo, Double moneyPaid) {
        this.user = user;
        this.room = room;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.moneyPaid = moneyPaid;
    }

    @Id
    @SequenceGenerator(name = "orderSeq", sequenceName = "ORDER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSeq")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    public User getUser() {
        return user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "ROOM_ID", nullable = false, updatable = false)
    public Room getRoom() {
        return room;
    }

    @Column(name = "DATE_FROM")
    public Date getDateFrom() {
        return dateFrom;
    }

    @Column(name = "DATE_TO")
    public Date getDateTo() {
        return dateTo;
    }

    @Column(name = "MONEY_PAID")
    public Double getMoneyPaid() {
        return moneyPaid;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setMoneyPaid(Double moneyPaid) {
        this.moneyPaid = moneyPaid;
    }
}