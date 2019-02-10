package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.Campsite;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by fernando on 09/02/19.
 */
public interface CampsiteRepository extends PagingAndSortingRepository<Campsite, Long> {
}
