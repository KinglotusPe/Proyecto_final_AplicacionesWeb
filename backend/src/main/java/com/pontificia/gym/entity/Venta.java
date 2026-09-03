package com.pontificia.gym.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente; // Opcional, puede ser venta a público general

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Cajero/recepcionista que realizó la venta

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago; // EFECTIVO, YAPE, PLIN, TARJETA

    @Column(name = "comprobante_numero", length = 30)
    private String comprobanteNumero;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VentaDetalle> detalles = new ArrayList<>();

    public Venta() {
    }

    public Venta(Long id, Cliente cliente, Usuario usuario, LocalDateTime fechaHora, BigDecimal total, String metodoPago, String comprobanteNumero) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.total = total;
        this.metodoPago = metodoPago;
        this.comprobanteNumero = comprobanteNumero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getComprobanteNumero() {
        return comprobanteNumero;
    }

    public void setComprobanteNumero(String comprobanteNumero) {
        this.comprobanteNumero = comprobanteNumero;
    }

    public List<VentaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }

    public String getCodigoComprobante() {
        if (comprobanteNumero != null && !comprobanteNumero.isEmpty()) {
            return comprobanteNumero;
        }
        return "TKT-" + String.format("%06d", id != null ? id : 0);
    }
}
