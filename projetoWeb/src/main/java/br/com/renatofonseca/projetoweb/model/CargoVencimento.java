package br.com.renatofonseca.projetoweb.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cargo_vencimento")
public class CargoVencimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargoID;
    
    @ManyToOne
    @JoinColumn(name = "vencimento_id")
    private Vencimentos vencimentoId;
}