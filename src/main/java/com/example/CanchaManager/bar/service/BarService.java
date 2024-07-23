//package com.example.CanchaManager.bar.service;
//
//import com.example.CanchaManager.bar.model.*;
//import com.example.CanchaManager.bar.repository.*;
//import com.example.CanchaManager.cancha.model.Reserva;
//import com.example.CanchaManager.cancha.repository.ReservaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BarService {
//    @Autowired
//    private MesaRepository mesaRepository;
//
//    @Autowired
//    private ProductoRepository productoRepository;
//
//    @Autowired
//    private ProveedorRepository proveedorRepository;
//
//    @Autowired
//    private ProductoMesaRepository productoMesaRepository;
//
//    @Autowired
//    private ProductoReservaRepository productoReservaRepository;
//
//    public Mesa crearMesa(Mesa mesa) {
//        return mesaRepository.save(mesa);
//    }
//
//    public Producto crearProducto(Producto producto) {
//        return productoRepository.save(producto);
//    }
//
//    public Proveedor crearProveedor(Proveedor proveedor) {
//        return proveedorRepository.save(proveedor);
//    }
//
//    public ProductoMesa agregarProductoAMesa(Long mesaId, Long productoId, Integer cantidad) {
//        Mesa mesa = mesaRepository.findById(mesaId).orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
//        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
//
//        ProductoMesa productoMesa = new ProductoMesa();
//        productoMesa.setMesa(mesa);
//        productoMesa.setProducto(producto);
//        productoMesa.setCantidad(cantidad);
//
//        return productoMesaRepository.save(productoMesa);
//    }
//
//    public ProductoReserva agregarProductoAReserva(Long reservaId, Long productoId, Integer cantidad) {
//        Reserva reserva = productoReservaRepository.findById(reservaId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
//        Producto producto = productoReservaRepository.findById(productoId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
//
//        ProductoReserva productoReserva = new ProductoReserva();
//        productoReserva.setReserva(reserva);
//        productoReserva.setProducto(producto);
//        productoReserva.setCantidad(cantidad);
//
//        return productoMesaRepository.save(productoReserva);
//    }
//}
