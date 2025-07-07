package com.example.booking_hotel.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.Posts;

import lombok.NonNull;

@Repository
public interface PostRepository extends JpaRepository<Posts, String> {
    Page<Posts> findAll(@NonNull Pageable pageable);

    @Query(
            """
	SELECT p FROM Posts p
	WHERE (:city IS NULL OR p.city = :city)
	AND (:district IS NULL OR p.district = :district)
	AND (:roomType IS NULL OR p.placeType.id = :roomType)
	AND (:minPrice IS NULL OR p.nightPrice >= :minPrice)
	AND (:maxPrice IS NULL OR p.nightPrice <= :maxPrice)
	AND (:maxPrice IS NULL OR p.nightPrice <= :maxPrice)
	AND (:amenitiesList IS NULL
		OR EXISTS (
			SELECT 1 FROM p.amenities a WHERE a.id IN (:amenitiesList)
		))
	""")
    Page<Posts> filterRoom(
            @Param("city") String city,
            @Param("district") String district,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minPrice") BigDecimal minPrice,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
            @Param("amenitiesList") List<String> amenitiesList,
            @Param("roomType") String roomType,
            Pageable pageable);
}
//
//AND ((:startDate IS NULL OR :endDate IS NULL)
//OR NOT EXISTS (
//		SELECT 1 FROM PostsAvailability pa
//				WHERE pa.post = p
//				AND pa.date BETWEEN :startDate AND :endDate
//				AND pa.status = IN ('DRAFT', 'LOCK')
//            ))
