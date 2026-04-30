package com.proveedores.portal.repository;

import com.proveedores.portal.entity.EstadoPago;
import com.proveedores.portal.entity.Pago;
import com.proveedores.portal.entity.TipoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByActivoTrue();

    List<Pago> findByEstadoAndActivoTrue(EstadoPago estado);

    List<Pago> findByTipoPagoAndActivoTrue(TipoPago tipoPago);

    List<Pago> findByProveedorIdAndActivoTrue(Long proveedorId);

    List<Pago> findByMesCorrespondienteAndAnioCorrespondienteAndActivoTrue(Integer mesCorrespondiente,
                                                                            Integer anioCorrespondiente);

    List<Pago> findByEstadoAndFechaVencimientoBeforeAndActivoTrue(EstadoPago estado, LocalDate fecha);

    @Query("select case when count(p) > 0 then true else false end " +
        "from Pago p " +
        "where lower(p.descripcion) = lower(:descripcion) " +
        "and p.tipoPago = :tipoPago " +
        "and p.mesCorrespondiente = :mesCorrespondiente " +
        "and p.anioCorrespondiente = :anioCorrespondiente " +
        "and p.activo = true " +
        "and ((:proveedorId is null and p.proveedor is null) or p.proveedor.id = :proveedorId)")
    boolean existsByDescripcionIgnoreCaseAndProveedorIdAndMesCorrespondienteAndAnioCorrespondienteAndTipoPago(
        @Param("descripcion") String descripcion,
        @Param("proveedorId") Long proveedorId,
        @Param("mesCorrespondiente") Integer mesCorrespondiente,
        @Param("anioCorrespondiente") Integer anioCorrespondiente,
        @Param("tipoPago") TipoPago tipoPago);

    @Query("select case when count(p) > 0 then true else false end " +
        "from Pago p " +
        "where p.id <> :id " +
        "and lower(p.descripcion) = lower(:descripcion) " +
        "and p.tipoPago = :tipoPago " +
        "and p.mesCorrespondiente = :mesCorrespondiente " +
        "and p.anioCorrespondiente = :anioCorrespondiente " +
        "and p.activo = true " +
        "and ((:proveedorId is null and p.proveedor is null) or p.proveedor.id = :proveedorId)")
    boolean existsDuplicadoExcluyendoId(@Param("id") Long id,
                                        @Param("descripcion") String descripcion,
                                        @Param("proveedorId") Long proveedorId,
                                        @Param("mesCorrespondiente") Integer mesCorrespondiente,
                                        @Param("anioCorrespondiente") Integer anioCorrespondiente,
                                        @Param("tipoPago") TipoPago tipoPago);

    @Query("select p from Pago p " +
        "left join fetch p.proveedor " +
        "where p.activo = true " +
        "and (:estado is null or p.estado = :estado) " +
        "and (:tipoPago is null or p.tipoPago = :tipoPago) " +
        "and (:proveedorId is null or p.proveedor.id = :proveedorId) " +
        "and (:mes is null or p.mesCorrespondiente = :mes) " +
        "and (:anio is null or p.anioCorrespondiente = :anio)")
    List<Pago> buscarPorFiltros(@Param("estado") EstadoPago estado,
                                @Param("tipoPago") TipoPago tipoPago,
                                @Param("proveedorId") Long proveedorId,
                                @Param("mes") Integer mes,
                                @Param("anio") Integer anio);
}
