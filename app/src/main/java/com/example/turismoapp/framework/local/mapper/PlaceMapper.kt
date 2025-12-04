package com.example.turismoapp.framework.local.mapper

import com.example.turismoapp.framework.dto.PlaceDto
import com.example.turismoapp.framework.local.entity.DestinationEntity

fun PlaceDto.toEntity() = DestinationEntity(
    id = id,
    name = name,
    description = description,
    rating = rating,
    city = city,
    department = department,
    imageUrl = imageUrl,
    latitude = latitude,
    longitude = longitude
)

fun DestinationEntity.toDto() = PlaceDto(
    id = id,
    name = name,
    description = description,
    rating = rating,
    city = city,
    department = department,
    imageUrl = imageUrl,
    latitude = latitude,
    longitude = longitude
)
