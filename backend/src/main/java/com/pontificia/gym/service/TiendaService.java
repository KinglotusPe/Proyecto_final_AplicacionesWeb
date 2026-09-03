package com.pontificia.gym.service;

import com.pontificia.gym.entity.CategoriaProducto;
import com.pontificia.gym.entity.Producto;
import com.pontificia.gym.entity.Venta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TiendaService {
    List<Producto> listarProductos();
    List<Producto> listarPorCategoria(Long categoriaId);
    List<Producto> buscarProductos(String query);
    Optional<Producto> buscarProductoPorId(Long id);
    Producto guardarProducto(Producto producto);
    void eliminarProducto(Long id);

    List<CategoriaProducto> listarCategorias();
    CategoriaProducto guardarCategoria(CategoriaProducto categoria);

    Venta registrarVenta(Long clienteId, String usernameCajero, String metodoPago, List<ItemVentaDto> items);
    List<Venta> listarVentas();
    Optional<Venta> buscarVentaPorId(Long id);
    BigDecimal calcularTotalVentasHoy();

    void inicializarCatalogoPorDefecto();

    public static class ItemVentaDto {
        private Long productoId;
        private Integer cantidad;

        public ItemVentaDto() {
        }

        public ItemVentaDto(Long productoId, Integer cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}
