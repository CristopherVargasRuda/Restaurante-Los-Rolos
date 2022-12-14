package com.rolosdev.seminarioproject.services.implementacionesServices;

import com.rolosdev.seminarioproject.entity.*;
import com.rolosdev.seminarioproject.repository.*;
import com.rolosdev.seminarioproject.services.interfacesServices.IRegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service("registroService")
@Transactional
public class RegistroService implements IRegistroService {

    @Autowired
    @Qualifier("administradorRepository")
    private IAdministradorRepository administradorRepository;

    @Autowired
    @Qualifier("clienteRepository")
    private IClienteRepository clienteRepository;

    @Autowired
    @Qualifier("ingredienteRepository")
    private IIngredienteRepository ingredienteRepository;

    @Autowired
    @Qualifier("menuRepository")
    private IMenuRepository menuRepository;

    @Autowired
    @Qualifier("restauranteRepository")
    private IRestauranteRepository restauranteRepository;

    @Autowired
    @Qualifier("productoRepository")
    private IProductoRepository productoRepository;

    @Autowired
    @Qualifier("productoIngredienteRepository")
    private IProductoIngredienteRepository productoIngredienteRepository;

    @Autowired
    @Qualifier("seleccionRepository")
    private ISeleccionRepository seleccionRepository;

    @Autowired
    @Qualifier("stockRepository")
    private IStockRepository stockRepository;

    @Override
    public String registrarCliente(Cliente cliente) {
        if (clienteRepository.verificarExistencia(cliente.getCorreo()) == null) {
            cliente.setIdCliente(clienteRepository.obtenerUltimoId().getIdCliente() + 1);
            clienteRepository.save(cliente);
            return "OK";
        }
        return "El correo " + cliente.getCorreo() + " Ya existe, verifique los datos";
    }
    @Override
    public String registrarAdministrador(Administrador administrador) {
        if (administradorRepository.verificarExistencia(administrador.getUserAdministrador()) == null) {
            administrador.setAdministradorId(administradorRepository.obtenerUltimoId().getAdministradorId() + 1);
            administradorRepository.save(administrador);
            return "OK";
        }
        return "El usuario " + administrador.getUserAdministrador() + " Ya existe, verifique los datos";
    }
    @Override
    public String registrarRestaurante(Restaurante restaurante) {
        if (restauranteRepository.verificarExistencia(restaurante.getUser()) == null) {
            restaurante.setIdRestaurante(restauranteRepository.obtenerUltimoId().getIdRestaurante() + 1);
            restauranteRepository.save(restaurante);
            return "OK";
        }
        return "El usuario " + restaurante.getIdRestaurante() + " Ya existe, verifique los datos";
    }

    @Override
    public String registrarIngrediente(Ingrediente ingrediente) {
        if (ingredienteRepository.verificarExistencia(ingrediente.getNombre()) == null) {
            ingrediente.setIdIngrediente(ingredienteRepository.obtenerUltimoId().getIdIngrediente() + 1);
            ingredienteRepository.save(ingrediente);
            return "OK";
        }
        return "El ingrediente " + ingrediente.getNombre() + " ya existe.";
    }

    @Override
    public String registrarProducto(Producto producto) {
        if (productoRepository.verificarExistencia(producto.getIdRestaurante(), producto.getNombre()) == null) {
            producto.setIdProducto(productoRepository.obtenerUltimoId().getIdProducto() + 1);
            productoRepository.save(producto);
            return "OK";
        }
        return "El Producto " + producto.getNombre() + " ya existe.";
    }

    @Override
    public String registrarMenu(Menu menu) {
        if (menuRepository.verificarExistencia(menu.getIdRestaurante(), menu.getNombre()) == null) {
            menu.setIdMenu(menuRepository.obtenerUltimoId().getIdMenu() + 1);
            menuRepository.save(menu);
            return "OK";
        }
        return "El Menu " + menu.getNombre() + " ya existe.";
    }

    @Override
    public String registrarProductoIngrediente(ProductoIngrediente productoIngrediente) {
        productoIngrediente.setIdProductoIngrediente(productoIngredienteRepository.obtenerUltimoId().getIdProductoIngrediente() + 1);
        productoIngredienteRepository.save(productoIngrediente);
        return "OK";
    }

    @Override
    public String registrarSeleccion(Seleccion seleccion) {
        seleccion.setIdSeleccion(seleccionRepository.obtenerUltimoId().getIdSeleccion() + 1);
        seleccionRepository.save(seleccion);
        return "OK";
    }

    @Override
    public void pruebas() {
        boolean confirmar;
        ArrayList<Producto> productosAEliminar = new ArrayList<>();
        ArrayList<Integer> idEliminados = new ArrayList<>();
        ArrayList<Producto> opcionesProductosMenu = productoRepository.obtenerProductosPorMenu(1);
        ArrayList<Ingrediente> ingredientesParaTodosLosProductos = ingredienteRepository.obtenerIngredientesPorMenu(1);
        ArrayList<Stock> stocks = stockRepository.obtenerStockPorMenu(1);
        ArrayList<Seleccion> seleccionesMenu = seleccionRepository.obtenerSeleccionPorMenu(1);
        for (Seleccion seleccion: seleccionesMenu) {
            System.out.println(seleccion.getIdSeleccion());
        }
        ArrayList<ProductoIngrediente> productosIngredientes = productoIngredienteRepository.obtenerProductoIngredientePorMenu(1);
        for (Producto producto : opcionesProductosMenu) {
            for (ProductoIngrediente productoIngrediente : productosIngredientes) {
                if (productoIngrediente.getIdProducto() == producto.getIdProducto()) {
                    for (Stock stock : stocks) {
                        if (stock.getIdRestaurante() == producto.getIdRestaurante() && productoIngrediente.getIdIngrediente() == stock.getIdIngrediente()) {
                            System.out.println("Cantidad en stock: " + stock.getCantidadStock() + " - ingrediente: " + stock.getIdIngrediente());
                            System.out.println("Cantidad de producto: " + productoIngrediente.getCantidad() + " - ingrediente: " + productoIngrediente.getIdIngrediente());
                            if (stock.getCantidadStock() < productoIngrediente.getCantidad()) {
                                System.out.println("Eliminar----------------------------------------------------------------");
                                confirmar = true;
                                for (Producto productoAEliminar : productosAEliminar) {
                                    if (productoAEliminar.equals(producto)) {
                                        confirmar = false;
                                        break;
                                    }
                                }
                                if (confirmar) {
                                    productosAEliminar.add(producto);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Producto producto : productosAEliminar) {
            opcionesProductosMenu.remove(producto);
        }
        for (Producto producto : opcionesProductosMenu) {
            for (ProductoIngrediente productoIngrediente : productosIngredientes) {
                if (productoIngrediente.getIdProducto() == producto.getIdProducto()) {
                    for (Stock stock : stocks) {
                        if (stock.getIdRestaurante() == producto.getIdRestaurante() && productoIngrediente.getIdIngrediente() == stock.getIdIngrediente()) {
                            stock.setCantidadStock(stock.getCantidadStock() - productoIngrediente.getCantidad());
                            stockRepository.save(stock);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void eliminarProducto(int id) {
        ArrayList<ProductoIngrediente> productoIngredientes = productoIngredienteRepository.obtenerProductoIngredietePorProducto(id);
        for (ProductoIngrediente pi : productoIngredientes) {
            productoIngredienteRepository.delete(pi);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public void eliminarMenu(int id) {
        ArrayList<Seleccion> selecciones = seleccionRepository.obtenerSeleccionPorMenu(id);
        for (Seleccion seleccion : selecciones) {
            seleccionRepository.delete(seleccion);
        }
        menuRepository.deleteById(id);
    }

}
