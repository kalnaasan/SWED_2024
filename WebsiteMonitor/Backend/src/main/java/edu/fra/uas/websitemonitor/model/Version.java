package edu.fra.uas.websitemonitor.model;

import edu.fra.uas.websitemonitor.observer.Observable;
import edu.fra.uas.websitemonitor.observer.Observer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
@Table(name = "versions")
public class Version implements Observable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content", length = 16777216)
    private String content;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "subscription_Id", nullable = false)
    private Subscription subscription;
    @Transient
    private Set<Observer> observers = new HashSet<>();

    public Version(String content, LocalDateTime now, Subscription subscription) {
        this.content = content;
        this.createdAt = now;
        this.subscription = subscription;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        this.observers.forEach(observer -> observer.update(this));
    }
}
