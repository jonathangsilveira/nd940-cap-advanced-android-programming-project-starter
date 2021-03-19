package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative

class RemoteRepresentativeDataSource(private val webservice: CivicsApiService)
    : RepresentativeDataSource {
    override suspend fun get(address: Address): List<Representative> {
        val (offices, officials) = webservice.representatives(address.toFormattedString())
        return offices.flatMap { office -> office.getRepresentatives(officials) }
    }

    override fun states(): List<String> {
        return listOf(
                "Alabama",
                "Alaska",
                "Arizona",
                "Arkansas",
                "California",
                "Colorado",
                "Connecticut",
                "Delaware",
                "Florida",
                "Georgia",
                "Hawaii",
                "Idaho",
                "Illinois",
                "Indiana",
                "Iowa",
                "Kansas",
                "Kentucky",
                "Louisiana",
                "Maine",
                "Maryland",
                "Massachusetts",
                "Michigan",
                "Minnesota",
                "Mississippi",
                "Missouri",
                "Montana",
                "Nebraska",
                "Nevada",
                "New Hampshire",
                "New Jersey",
                "New Mexico",
                "New York",
                "North Carolina",
                "North Dakota",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Rhode Island",
                "South Carolina",
                "South Dakota",
                "Tennessee",
                "Texas",
                "Utah",
                "Vermont",
                "Virginia",
                "Washington",
                "West Virginia",
                "Wisconsin",
                "Wyoming"
        )
    }
}