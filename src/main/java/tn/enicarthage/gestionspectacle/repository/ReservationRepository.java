package tn.enicarthage.gestionspectacle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.enicarthage.gestionspectacle.model.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countBySpectacleDateLieuId(Long spectacleDateLieuId);

    List<Reservation> findByUtilisateurId(Long userId);

    // Ajout d'une méthode pour trouver les réservations par date/lieu
    List<Reservation> findBySpectacleDateLieuId(Long spectacleDateLieuId);
    List<Reservation> findByUtilisateurIdAndPaymentStatus(Long userId, String paymentStatus);
    List<Reservation> findByUtilisateurIdOrderByDateReservationDesc(Long userId);

    @Modifying
    @Query("UPDATE Reservation r SET r.paymentStatus = :status WHERE r.id = :id")
    void updatePaymentStatus(@Param("id") Long id, @Param("status") String status);


    @Query("SELECT COALESCE(SUM(r.nbPlaces), 0) FROM Reservation r " +
            "WHERE r.spectacleDateLieu.id = :dateLieuId AND r.paymentStatus = 'PAID'")
    int countPaidReservationsForDateLieu(@Param("dateLieuId") Long dateLieuId);
}