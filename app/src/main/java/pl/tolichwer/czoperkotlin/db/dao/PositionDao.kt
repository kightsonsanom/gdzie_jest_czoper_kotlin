package pl.tolichwer.czoperkotlin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import pl.tolichwer.czoperkotlin.model.Position

@Dao
interface PositionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(positionList: List<Position>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosition(position: Position): Single<Long>

    @Query("SELECT * FROM position WHERE position.userID= :userID ORDER BY lastLocationDate DESC LIMIT 1")
    fun getLatestPositionFromDB(userID: Int): Single<Position>


}