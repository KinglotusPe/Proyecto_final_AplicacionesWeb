package com.pontificia.gym.service.impl;

import com.pontificia.gym.entity.*;
import com.pontificia.gym.repository.*;
import com.pontificia.gym.service.TiendaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TiendaServiceImpl implements TiendaService {

    private final ProductoRepository productoRepository;
    private final CategoriaProductoRepository categoriaProductoRepository;
    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public TiendaServiceImpl(ProductoRepository productoRepository,
                             CategoriaProductoRepository categoriaProductoRepository,
                             VentaRepository ventaRepository,
                             VentaDetalleRepository ventaDetalleRepository,
                             ClienteRepository clienteRepository,
                             UsuarioRepository usuarioRepository) {
        this.productoRepository = productoRepository;
        this.categoriaProductoRepository = categoriaProductoRepository;
        this.ventaRepository = ventaRepository;
        this.ventaDetalleRepository = ventaDetalleRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarProductos(String query) {
        if (query == null || query.trim().isEmpty()) {
            return listarProductos();
        }
        return productoRepository.findByNombreContainingIgnoreCase(query.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaProducto> listarCategorias() {
        return categoriaProductoRepository.findAll();
    }

    @Override
    public CategoriaProducto guardarCategoria(CategoriaProducto categoria) {
        return categoriaProductoRepository.save(categoria);
    }

    @Override
    public Venta registrarVenta(Long clienteId, String usernameCajero, String metodoPago, List<ItemVentaDto> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("No hay productos en el carrito de compras.");
        }

        Cliente cliente = clienteId != null ? clienteRepository.findById(clienteId).orElse(null) : null;
        Usuario usuario = usernameCajero != null ? usuarioRepository.findByUsername(usernameCajero).orElse(null) : null;

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta.setFechaHora(LocalDateTime.now());
        venta.setMetodoPago(metodoPago != null ? metodoPago.toUpperCase() : "EFECTIVO");

        Venta savedVenta = ventaRepository.save(venta);
        savedVenta.setComprobanteNumero("TKT-" + String.format("%06d", savedVenta.getId()));

        BigDecimal totalVenta = BigDecimal.ZERO;
        List<VentaDetalle> detalles = new ArrayList<>();

        for (ItemVentaDto item : items) {
            Producto prod = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado ID: " + item.getProductoId()));

            int cant = item.getCantidad() != null && item.getCantidad() > 0 ? item.getCantidad() : 1;

            if (prod.getStock() < cant) {
                throw new IllegalStateException("Stock insuficiente para: " + prod.getNombre() + " (Disponibles: " + prod.getStock() + ")");
            }

            // Descontar inventario
            prod.setStock(prod.getStock() - cant);
            productoRepository.save(prod);

            BigDecimal subtotal = prod.getPrecioUnitario().multiply(BigDecimal.valueOf(cant));
            totalVenta = totalVenta.add(subtotal);

            VentaDetalle det = new VentaDetalle(null, savedVenta, prod, cant, prod.getPrecioUnitario(), subtotal);
            detalles.add(ventaDetalleRepository.save(det));
        }

        savedVenta.setTotal(totalVenta);
        savedVenta.setDetalles(detalles);
        return ventaRepository.save(savedVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarVentas() {
        return ventaRepository.findAllByOrderByFechaHoraDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVentasHoy() {
        LocalDateTime inicioHoy = LocalDate.now().atStartOfDay();
        LocalDateTime finHoy = LocalDate.now().atTime(23, 59, 59);
        return ventaRepository.calcularTotalVentasEntre(inicioHoy, finHoy);
    }

    @Override
    public void inicializarCatalogoPorDefecto() {
        if (categoriaProductoRepository.count() == 0) {
            CategoriaProducto c1 = categoriaProductoRepository.save(new CategoriaProducto(null, "Proteínas & Suplementos", "Proteínas Whey, Creatinas, Aminoácidos y Quemadores"));
            CategoriaProducto c2 = categoriaProductoRepository.save(new CategoriaProducto(null, "Bebidas & Hidratación", "Bebidas rehidratantes, energéticas y agua mineral"));
            CategoriaProducto c3 = categoriaProductoRepository.save(new CategoriaProducto(null, "Accesorios & Candados", "Candados de casillero, shakers, toallas y guantes"));
            CategoriaProducto c4 = categoriaProductoRepository.save(new CategoriaProducto(null, "Snacks & Barras", "Barras hiperproteicas y frutos secos"));

            if (productoRepository.count() == 0) {
                productoRepository.save(new Producto(null, c1, "Proteína 100% Whey Gold Standard 2lb", "Proteína aislada de suero de leche sabor vainilla", new BigDecimal("185.00"), 15, "https://images.unsplash.com/photo-1579722820308-d74e571900a9?w=300&auto=format&fit=crop&q=80", "775123456001"));
                productoRepository.save(new Producto(null, c1, "Creatina Monohidratada Creapure 300g", "Creatina pura micronizada 100% fuerza y recuperación", new BigDecimal("110.00"), 20, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=300&auto=format&fit=crop&q=80", "775123456002"));
                productoRepository.save(new Producto(null, c2, "Bebida Rehidratante Gatorade 500ml", "Electrolitos sabor mora / frutos tropicales", new BigDecimal("4.50"), 45, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&auto=format&fit=crop&q=80", "775123456003"));
                productoRepository.save(new Producto(null, c2, "Agua Mineral San Mateo 600ml", "Agua purificada sin gas de manantial", new BigDecimal("2.50"), 60, "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=300&auto=format&fit=crop&q=80", "775123456004"));
                productoRepository.save(new Producto(null, c3, "Candado de Seguridad Combinación 4 Dígitos", "Candado reforzado de aleación para casilleros / lockers", new BigDecimal("22.00"), 30, "https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=300&auto=format&fit=crop&q=80", "775123456005"));
                productoRepository.save(new Producto(null, c3, "Shaker Mezclador BRUTAL 700ml", "Vaso mezclador con bola batidora y compartimento", new BigDecimal("18.00"), 25, "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=300&auto=format&fit=crop&q=80", "775123456006"));
                productoRepository.save(new Producto(null, c4, "Barra de Proteína Quest Bar 60g", "21g de proteína y 1g de azúcar sabor chocolate chip", new BigDecimal("12.00"), 40, "https://images.unsplash.com/photo-1622484216850-2580536c1e55?w=300&auto=format&fit=crop&q=80", "775123456007"));
            }
        }
    }
}
