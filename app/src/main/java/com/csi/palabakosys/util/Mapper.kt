package com.csi.palabakosys.util

interface Mapper<Entity, Domain> {
    fun mapFromEntity(entity: Entity) : Domain
    fun mapToEntity(domain: Domain) : Entity
}