package org.entur.norgesturbackend.norgestur.controller

import org.entur.norgesturbackend.norgestur.config.MyProperties
import org.entur.norgesturbackend.norgestur.model.PlayerScore
import org.entur.norgesturbackend.norgestur.model.PlayerScoreDto
import org.entur.norgesturbackend.norgestur.model.toResponse
import org.entur.norgesturbackend.norgestur.service.PlayerScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class PlayerScoreController (val playerScoreService: PlayerScoreService){

    @Autowired
    lateinit var myProperties: MyProperties

    @GetMapping("/player-score")
    fun getPlayerScoreByDifficulty(
            @RequestParam difficulty: String,
            @RequestParam(defaultValue = "20") size: Number
    ): List<PlayerScoreDto> {
            return playerScoreService.getScoreByDifficultyAndSize(difficulty.lowercase(), size).map { it.toResponse() }
    }

    @GetMapping("/player-score/top-ten-overall")
    fun getPlayerScore(): List<PlayerScoreDto> {
        return playerScoreService.getTopTen().map { it.toResponse() }
    }

    @PostMapping("/player-score")
    fun addPlayerScore(
            @RequestBody playerScore: PlayerScore,
            @RequestHeader("Auth") secret: String
    ): HttpStatus{

        if ( secret != myProperties.secret) return HttpStatus.BAD_REQUEST

        return playerScoreService.savePlayerScore(playerScore)
    }

    @GetMapping("/player-score/end-game")
    fun getTextForOptimalRoute(
        @RequestParam difficulty: String
    ): String {
        return playerScoreService.getOptimalRouteText(difficulty.lowercase())
    }
}