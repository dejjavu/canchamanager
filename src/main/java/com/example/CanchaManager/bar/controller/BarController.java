//package com.example.CanchaManager.bar.controller;
//import com.example.CanchaManager.bar.model.Mesa;
//import com.example.CanchaManager.bar.model.Producto;
//import com.example.CanchaManager.bar.model.ProductoMesa;
//import com.example.CanchaManager.bar.model.Proveedor;
//import com.example.CanchaManager.bar.service.BarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/bar")
//public class BarController {
//    @Autowired
//    private BarService barService;
//
//    @PostMapping("/mesas")
//    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa) {
//        return ResponseEntity.ok(barService.crearMesa(mesa));
//    }
//
//    @PostMapping("/productos")
//    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
//        return ResponseEntity.ok(barService.crearProducto(producto));
//    }
//
//    @PostMapping("/proveedores")
//    public ResponseEntity<Proveedor> crearProveedor(@RequestBody Proveedor proveedor) {
//        return ResponseEntity.ok(barService.crearProveedor(proveedor));
//    }
//
//    @PostMapping("/mesas/{mesaId}/productos/{productoId}")
//    public ResponseEntity<ProductoMesa> agregarProductoAMesa(@PathVariable Long mesaId, @PathVariable Long productoId, @RequestParam Integer cantidad) {
//        return ResponseEntity.ok(barService.agregarProductoAMesa(mesaId, productoId, cantidad));
//    }
//}
