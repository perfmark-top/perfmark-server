package io.github.perfmarktop.mariadb

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:21
 */
@Repository
interface CpuPerfMarkDao: JpaRepository<CpuPerfMarkEntity, Int> {

}

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:21
 */
@Repository
interface GpuPerfMarkDao: JpaRepository<GpuPerfMarkEntity, Int> {
}

/**
 * @author Madray Haven
 * @Date 2023/6/21 上午10:14
 */
@Repository
interface UpdateLogRepository: JpaRepository<UpdateLogEntity, UpdateLogEntity.Type> {

}
