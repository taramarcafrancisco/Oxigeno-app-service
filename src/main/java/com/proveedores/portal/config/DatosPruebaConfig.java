package com.proveedores.portal.config;

import com.proveedores.portal.dto.CrearOrdenCompraRequest;
import com.proveedores.portal.dto.OrdenCompraItemRequest;
import com.proveedores.portal.dto.ProductoRequest;
import com.proveedores.portal.dto.ProveedorRequest;
import com.proveedores.portal.entity.Categoria;
import com.proveedores.portal.entity.Rol;
import com.proveedores.portal.entity.Usuario;
import com.proveedores.portal.repository.CategoriaRepository;
import com.proveedores.portal.repository.ProductoRepository;
import com.proveedores.portal.repository.ProveedorRepository;
import com.proveedores.portal.repository.RolRepository;
import com.proveedores.portal.repository.UsuarioRepository;
import com.proveedores.portal.services.OrdenCompraService;
import com.proveedores.portal.services.ProductoService;
import com.proveedores.portal.services.ProveedorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class DatosPruebaConfig {

    @Bean
    CommandLineRunner insertarDatosPrueba(ProveedorRepository proveedorRepository,
                                          ProductoRepository productoRepository,
                                          CategoriaRepository categoriaRepository,
                                          RolRepository rolRepository,
                                          UsuarioRepository usuarioRepository,
                                          ProveedorService proveedorService,
                                          ProductoService productoService,
                                          OrdenCompraService ordenCompraService,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            Rol admin = asegurarRol(rolRepository, "ADMIN");
            Rol operador = asegurarRol(rolRepository, "OPERADOR");
            asegurarUsuario(usuarioRepository, passwordEncoder, admin, operador);

            if (proveedorRepository.count() > 0 || productoRepository.count() > 0) {
                return;
            }

            Categoria categoria = categoriaRepository.save(Categoria.builder()
                .nombre("Insumos generales")
                .descripcion("Productos y servicios habituales de proveedores")
                .build());

            ProveedorRequest proveedorUno = proveedor("Acme Suministros S.A.", "30-11111111-1",
                "compras@acme.com", "011-4000-1000", "Av. Corrientes 1234");
            ProveedorRequest proveedorDos = proveedor("Logistica Integral SRL", "30-22222222-2",
                "ventas@logisticaintegral.com", "011-4000-2000", "Ruta 8 Km 45");

            Long proveedorUnoId = proveedorService.crearProveedor(proveedorUno).getId();
            proveedorService.crearProveedor(proveedorDos);

            Long productoUnoId = productoService.altaProducto(producto("Toner impresora",
                "Toner negro compatible para equipos administrativos", new BigDecimal("28500.00"),
                25, proveedorUnoId, categoria.getId())).getId();
            Long productoDosId = productoService.altaProducto(producto("Resmas A4",
                "Papel A4 75g caja por 10 unidades", new BigDecimal("42500.00"),
                40, proveedorUnoId, categoria.getId())).getId();
            productoService.altaProducto(producto("Servicio de traslado",
                "Traslado urbano para paquetes corporativos", new BigDecimal("15000.00"),
                10, proveedorUnoId, categoria.getId()));

            CrearOrdenCompraRequest orden = new CrearOrdenCompraRequest();
            orden.setProveedorId(proveedorUnoId);
            orden.setItems(Arrays.asList(item(productoUnoId, 2), item(productoDosId, 1)));
            ordenCompraService.crearOrden(orden);
        };
    }

    private Rol asegurarRol(RolRepository rolRepository, String nombre) {
        Rol rol = rolRepository.findByNombre(nombre);
        if (rol != null) {
            return rol;
        }
        return rolRepository.save(new Rol(null, nombre));
    }

    private void asegurarUsuario(UsuarioRepository usuarioRepository,
                                 PasswordEncoder passwordEncoder,
                                 Rol admin,
                                 Rol operador) {
        if (usuarioRepository.findByEmail("admin@proveedores.local") != null) {
            return;
        }
        Usuario usuario = Usuario.builder()
            .nombre("Administrador")
            .email("admin@proveedores.local")
            .password(passwordEncoder.encode("admin123"))
            .activo(true)
            .roles(new HashSet<>(Arrays.asList(admin, operador)))
            .build();
        usuarioRepository.save(usuario);
    }

    private ProveedorRequest proveedor(String razonSocial, String cuit, String email, String telefono, String direccion) {
        ProveedorRequest request = new ProveedorRequest();
        request.setRazonSocial(razonSocial);
        request.setCuit(cuit);
        request.setEmail(email);
        request.setTelefono(telefono);
        request.setDireccion(direccion);
        request.setRubro("Insumos generales");
        request.setCondicionFiscal("Responsable inscripto");
        request.setDocumentacionEstado("VIGENTE");
        return request;
    }

    private ProductoRequest producto(String nombre, String descripcion, BigDecimal precio,
                                     Integer stock, Long proveedorId, Long categoriaId) {
        ProductoRequest request = new ProductoRequest();
        request.setNombre(nombre);
        request.setDescripcion(descripcion);
        request.setPrecio(precio);
        request.setStock(stock);
        request.setProveedorId(proveedorId);
        request.setCategoriaId(categoriaId);
        return request;
    }

    private OrdenCompraItemRequest item(Long productoId, Integer cantidad) {
        OrdenCompraItemRequest request = new OrdenCompraItemRequest();
        request.setProductoId(productoId);
        request.setCantidad(cantidad);
        return request;
    }
}
