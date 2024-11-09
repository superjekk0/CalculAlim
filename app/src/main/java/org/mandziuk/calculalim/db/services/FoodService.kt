package org.mandziuk.calculalim.db.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.mandziuk.calculalim.R
import org.mandziuk.calculalim.db.FoodDao
import org.mandziuk.calculalim.db.dtos.FoodDTO
import org.mandziuk.calculalim.db.dtos.FoodGroupDTO
import java.util.Locale
import kotlin.coroutines.suspendCoroutine

class FoodService(private val foodDao: FoodDao) {
    fun getFood() : List<FoodDTO>{

        return ArrayList();
    }

    suspend fun getFoodGroups(context: Context) : List<FoodGroupDTO>{
        return withContext(Dispatchers.IO){
            val groups = foodDao.getGroups();
            val locale = Locale.getDefault().displayLanguage;
            val liste = if (locale.contains("fr", true)){
                groups.map { g -> FoodGroupDTO(g.id, g.groupNameFr) }.toMutableList();
            } else {
                groups.map { g -> FoodGroupDTO(g.id, g.groupName) }.toMutableList();
            };
            liste.add(0, FoodGroupDTO(0, context.getString(R.string.tousGroupes)));
            return@withContext liste;
        }
    }
}