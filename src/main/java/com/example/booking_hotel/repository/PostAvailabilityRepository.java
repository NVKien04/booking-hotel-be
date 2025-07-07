package com.example.booking_hotel.repository;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.PostsAvailability;

@Repository
public interface PostAvailabilityRepository extends JpaRepository<PostsAvailability, String> {
    @Query(
            "SELECT p FROM PostsAvailability p WHERE p.post.id = :postId AND p.status = :status AND p.date >= :newCheckIn AND p.date < :newCheckOut")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<PostsAvailability> findLockPtAvailability(
            @Param("postId") String postId,
            @Param("status") String status,
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckOut);

    @Query("select p from PostsAvailability p where p.post.id = :postId and p.status = :status")
    List<PostsAvailability> findLockPtAvailabilityList(@Param("postId") String postId, @Param("status") String status);
}
