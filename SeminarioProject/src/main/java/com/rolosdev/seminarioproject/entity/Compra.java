package com.rolosdev.seminarioproject.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMPRA")
public class Compra {

    @Id
    @Column(name = "IDCOMPRA", nullable = false)
    private int idCompra;
    @Column(name = "IDCLIENTE", nullable = false)
    private int idCliente;
    @Column(name = "TOTAL", nullable = false)
    private double total;
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
