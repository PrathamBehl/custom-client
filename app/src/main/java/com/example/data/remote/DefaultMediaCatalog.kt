package com.example.data.remote

import com.example.data.model.ActivityItem
import com.example.data.model.MediaEntity
import com.example.data.model.MediaRelation
import com.example.data.model.ReleaseScheduleItem
import com.example.data.model.StreamSource
import com.example.data.model.SubtitleTrack
import com.example.data.model.UserListEntry

object DefaultMediaCatalog {

    val sampleMediaList: List<MediaEntity> = listOf(
        MediaEntity(
            id = 16498,
            malId = 16498,
            titleRomaji = "Shingeki no Kyojin",
            titleEnglish = "Attack on Titan: The Final Chapters",
            titleNative = "進撃の巨人",
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1600&auto=format&fit=crop&q=80",
            format = "TV",
            episodes = 28,
            chapters = null,
            durationMinutes = 24,
            aniListScore = 9.0f,
            malScore = 9.05f,
            genres = listOf("Action", "Drama", "Fantasy", "Mystery"),
            themes = listOf("Military", "Survival", "Gore", "Politics"),
            status = "FINISHED",
            season = "SPRING 2026",
            description = "Centuries ago, mankind was slaughtered to near extinction by monstrous humanoid creatures called Titans. Now the final struggle for survival and freedom reaches its cataclysmic conclusion across Paradis and the world.",
            source = "Manga",
            startDate = "2023-03-04",
            endDate = "2023-11-05",
            studios = listOf("MAPPA", "Wit Studio"),
            openings = listOf("The Rumbling - SiM", "Under the Tree - SiM"),
            endings = listOf("Akuma no Ko - Higuchi Ai", "To You 2,000... or... 20,000 Years From Now - Linked Horizon"),
            nextAiringEpisode = null,
            nextAiringSeconds = null,
            tagsWithRelevance = mapOf("Dark Fantasy" to 98, "Post-Apocalyptic" to 95, "Philosophy" to 89, "War" to 92),
            relations = listOf(
                MediaRelation(16497, "Attack on Titan Season 3", "PREQUEL", "TV", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80"),
                MediaRelation(16499, "Junior High", "SPIN_OFF", "TV", "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80")
            ),
            isFavorite = true
        ),
        MediaEntity(
            id = 153518,
            malId = 52991,
            titleRomaji = "Sousou no Frieren",
            titleEnglish = "Frieren: Beyond Journey's End",
            titleNative = "葬送のフリーレン",
            coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1600&auto=format&fit=crop&q=80",
            format = "TV",
            episodes = 28,
            chapters = null,
            durationMinutes = 24,
            aniListScore = 9.1f,
            malScore = 9.38f,
            genres = listOf("Adventure", "Drama", "Fantasy"),
            themes = listOf("Time Passage", "Magic", "Elves", "Nostalgia"),
            status = "RELEASING",
            season = "SUMMER 2026",
            description = "The demon king has been defeated, and the victorious hero party returns home before disbanding. As an elf with a lifespan of millennia, mage Frieren embarks on a quiet pilgrimage to truly understand human hearts.",
            source = "Manga",
            startDate = "2023-09-29",
            endDate = null,
            studios = listOf("Madhouse"),
            openings = listOf("Yuusha - YOASOBI", "Hareru - Yorushika"),
            endings = listOf("Anytime Anywhere - milet", "Bliss - milet"),
            nextAiringEpisode = 19,
            nextAiringSeconds = 12450L,
            tagsWithRelevance = mapOf("Melancholy" to 97, "Magic System" to 93, "Coming of Age" to 88, "Philosophy" to 90),
            relations = listOf(
                MediaRelation(153519, "Frieren Season 2", "SEQUEL", "TV", "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&auto=format&fit=crop&q=80")
            ),
            isFavorite = true
        ),
        MediaEntity(
            id = 142838,
            malId = 51009,
            titleRomaji = "Jujutsu Kaisen Season 2",
            titleEnglish = "Jujutsu Kaisen: Shibuya Incident",
            titleNative = "呪術廻戦 懐玉・玉折 / 渋谷事変",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1600&auto=format&fit=crop&q=80",
            format = "TV",
            episodes = 23,
            chapters = null,
            durationMinutes = 24,
            aniListScore = 8.8f,
            malScore = 8.83f,
            genres = listOf("Action", "Fantasy", "Supernatural"),
            themes = listOf("Curses", "Urban Fantasy", "Shounen", "Exorcists"),
            status = "FINISHED",
            season = "FALL 2025",
            description = "The past unravels Gojo Satoru and Geto Suguru's youth, leading directly into Halloween night at Shibuya where curses launch an all-out assault to seal humanity's strongest sorcerer.",
            source = "Manga",
            startDate = "2023-07-06",
            endDate = "2023-12-28",
            studios = listOf("MAPPA"),
            openings = listOf("Ao no Sumika - Tatsuya Kitani", "SPECIALZ - King Gnu"),
            endings = listOf("Akari - Soshi Sakiyama", "More Than Words - Hitsujibungaku"),
            nextAiringEpisode = null,
            nextAiringSeconds = null,
            tagsWithRelevance = mapOf("High Stakes" to 99, "Martial Arts" to 94, "Cursed Spirits" to 96, "Urban Warfare" to 91),
            relations = listOf(
                MediaRelation(142837, "Jujutsu Kaisen 0", "PREQUEL", "MOVIE", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80"),
                MediaRelation(142839, "Culling Game Arc", "SEQUEL", "TV", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80")
            )
        ),
        MediaEntity(
            id = 113415,
            malId = 40748,
            titleRomaji = "Jujutsu Kaisen (Manga)",
            titleEnglish = "Jujutsu Kaisen: Sorcery Fight",
            titleNative = "呪術廻戦",
            coverUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1600&auto=format&fit=crop&q=80",
            format = "MANGA",
            episodes = null,
            chapters = 271,
            durationMinutes = null,
            aniListScore = 8.5f,
            malScore = 8.52f,
            genres = listOf("Action", "Supernatural"),
            themes = listOf("Demons", "School Life"),
            status = "FINISHED",
            season = null,
            description = "Yuuji Itadori is a boy with tremendous physical strength, though he lives an ordinary high school life. One day, to save a friend who has been attacked by curses, he eats the finger of Ryomen Sukuna.",
            source = "Original",
            startDate = "2018-03-05",
            endDate = "2024-09-30",
            studios = listOf("Shueisha - Weekly Shonen Jump"),
            openings = emptyList(),
            endings = emptyList(),
            tagsWithRelevance = mapOf("Dark Fantasy" to 92, "Supernatural" to 90),
            relations = emptyList()
        ),
        MediaEntity(
            id = 154587,
            malId = 53393,
            titleRomaji = "Boku no Kokoro no Yabai Yatsu Season 2",
            titleEnglish = "The Dangers in My Heart S2",
            titleNative = "僕の心のヤバイやつ",
            coverUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1600&auto=format&fit=crop&q=80",
            format = "TV",
            episodes = 13,
            chapters = null,
            durationMinutes = 24,
            aniListScore = 8.7f,
            malScore = 8.81f,
            genres = listOf("Comedy", "Romance", "Slice of Life"),
            themes = listOf("School", "First Love", "Introversion", "Wholesome"),
            status = "FINISHED",
            season = "WINTER 2026",
            description = "Kyotaro Ichikawa may look like a shy middle schooler, but in his mind he harbors dark fantasies. Yet as he grows closer to class idol Anna Yamada, their tender vulnerability transforms his whole world.",
            source = "Manga",
            startDate = "2024-01-07",
            endDate = "2024-03-31",
            studios = listOf("Shin-Ei Animation"),
            openings = listOf("Boku wa... - Atarayo"),
            endings = listOf("Koi Shiteru Jibun Suki ni Nareta yo - Kohana Lam"),
            nextAiringEpisode = null,
            nextAiringSeconds = null,
            tagsWithRelevance = mapOf("Romance" to 99, "Wholesome" to 97, "Character Growth" to 95),
            relations = listOf(
                MediaRelation(154586, "The Dangers in My Heart S1", "PREQUEL", "TV", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80")
            )
        ),
        MediaEntity(
            id = 151807,
            malId = 52701,
            titleRomaji = "Solo Leveling: Arise",
            titleEnglish = "Solo Leveling: Season 2 - Arise from the Shadow",
            titleNative = "나 혼자만 레벨업",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1600&auto=format&fit=crop&q=80",
            format = "TV",
            episodes = 13,
            chapters = null,
            durationMinutes = 24,
            aniListScore = 8.6f,
            malScore = 8.67f,
            genres = listOf("Action", "Adventure", "Fantasy"),
            themes = listOf("Dungeons", "Level Up", "Shadow Monarch", "Monsters"),
            status = "RELEASING",
            season = "SUMMER 2026",
            description = "Known as the weakest hunter of all mankind, Sung Jinwoo awakens a secret player system in a dual dungeon that allows only him to see quests, earn stats, and command an invincible shadow army.",
            source = "Web Novel / Manhwa",
            startDate = "2026-07-02",
            endDate = null,
            studios = listOf("A-1 Pictures"),
            openings = listOf("LEveL - SawanoHiroyuki[nZk]:TOMORROW X TOGETHER"),
            endings = listOf("request - krage"),
            nextAiringEpisode = 9,
            nextAiringSeconds = 48200L,
            tagsWithRelevance = mapOf("Power Fantasy" to 98, "Necromancy" to 96, "Action Choreography" to 94),
            relations = listOf(
                MediaRelation(151806, "Solo Leveling Season 1", "PREQUEL", "TV", "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80")
            )
        ),
        MediaEntity(
            id = 1535,
            malId = 1535,
            titleRomaji = "Death Note",
            titleEnglish = "Death Note",
            titleNative = "DEATH NOTE",
            coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1600&auto=format&fit=crop&q=80",
            format = "TV",
            episodes = 37,
            chapters = null,
            durationMinutes = 23,
            aniListScore = 8.5f,
            malScore = 8.62f,
            genres = listOf("Mystery", "Psychological", "Supernatural", "Thriller"),
            themes = listOf("Mind Games", "Justice", "Detective", "Shinigami"),
            status = "FINISHED",
            season = "FALL 2006",
            description = "A brilliant high school student discovers a supernatural notebook that grants the ability to kill anyone whose face and name he knows, igniting an intense game of cat-and-mouse with genius detective L.",
            source = "Manga",
            startDate = "2006-10-04",
            endDate = "2007-06-27",
            studios = listOf("Madhouse"),
            openings = listOf("The WORLD - Nightmare", "What's up, people?! - Maximum the Hormone"),
            endings = listOf("Alumina - Nightmare", "Zetsubou Billy - Maximum the Hormone"),
            nextAiringEpisode = null,
            nextAiringSeconds = null,
            tagsWithRelevance = mapOf("Psychological" to 99, "Mind Games" to 98, "Anti-Hero" to 96)
        ),
        MediaEntity(
            id = 140960,
            malId = 50265,
            titleRomaji = "Spy x Family",
            titleEnglish = "SPY x FAMILY Code: White",
            titleNative = "SPY×FAMILY",
            coverUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=1600&auto=format&fit=crop&q=80",
            format = "MOVIE",
            episodes = 1,
            chapters = null,
            durationMinutes = 110,
            aniListScore = 8.3f,
            malScore = 8.44f,
            genres = listOf("Action", "Comedy", "Slice of Life"),
            themes = listOf("Espionage", "Found Family", "Telepathy", "Assassins"),
            status = "FINISHED",
            season = "WINTER 2024",
            description = "A spy on an undercover mission marries an assassin and adopts a telepathic girl, with none of them knowing the truth about each other's secrets.",
            source = "Manga",
            startDate = "2023-12-22",
            endDate = null,
            studios = listOf("Wit Studio", "CloverWorks"),
            openings = listOf("SOULSOUP - Official HIGE DANdism"),
            endings = listOf("Hikari no Ato - Gen Hoshino"),
            nextAiringEpisode = null,
            nextAiringSeconds = null,
            tagsWithRelevance = mapOf("Comedy" to 95, "Found Family" to 97, "Childcare" to 92)
        )
    )

    val sampleUserListEntries: List<UserListEntry> = listOf(
        UserListEntry(
            mediaId = 153518,
            title = "Frieren: Beyond Journey's End",
            coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            format = "TV",
            status = "WATCHING",
            progress = 18,
            totalUnits = 28,
            userScore = 9.5f,
            rewatchCount = 0,
            notes = "Episode 18 was peak magic animation! Waiting for Friday.",
            lastWatchedTimestamp = System.currentTimeMillis() - 3600000L
        ),
        UserListEntry(
            mediaId = 151807,
            title = "Solo Leveling: Arise",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            format = "TV",
            status = "WATCHING",
            progress = 8,
            totalUnits = 13,
            userScore = 8.5f,
            rewatchCount = 0,
            notes = "Shadow Monarch resurrection sequence is unmatched.",
            lastWatchedTimestamp = System.currentTimeMillis() - 7200000L
        ),
        UserListEntry(
            mediaId = 113415,
            title = "Jujutsu Kaisen: Sorcery Fight",
            coverUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800&auto=format&fit=crop&q=80",
            format = "MANGA",
            status = "READING",
            progress = 265,
            totalUnits = 271,
            userScore = 9.0f,
            rewatchCount = 1,
            notes = "Reading the climax battle chapters.",
            lastWatchedTimestamp = System.currentTimeMillis() - 14400000L
        ),
        UserListEntry(
            mediaId = 16498,
            title = "Attack on Titan: The Final Chapters",
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80",
            format = "TV",
            status = "COMPLETED",
            progress = 28,
            totalUnits = 28,
            userScore = 10.0f,
            rewatchCount = 2,
            notes = "Masterpiece conclusion. Rewatched with commentary.",
            lastWatchedTimestamp = System.currentTimeMillis() - 86400000L
        )
    )

    val sampleSchedule: List<ReleaseScheduleItem> = listOf(
        ReleaseScheduleItem(
            mediaId = 153518,
            title = "Frieren: Beyond Journey's End",
            coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            episode = 19,
            dayOfWeek = 5, // Friday
            airTime = "23:00 JST",
            countdownSeconds = 12450L,
            aniListScore = 9.1f
        ),
        ReleaseScheduleItem(
            mediaId = 151807,
            title = "Solo Leveling: Arise",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            episode = 9,
            dayOfWeek = 6, // Saturday
            airTime = "24:00 JST",
            countdownSeconds = 48200L,
            aniListScore = 8.6f
        ),
        ReleaseScheduleItem(
            mediaId = 142838,
            title = "Jujutsu Kaisen Season 2 Special",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            episode = 24,
            dayOfWeek = 4, // Thursday
            airTime = "23:56 JST",
            countdownSeconds = 5400L,
            aniListScore = 8.8f
        ),
        ReleaseScheduleItem(
            mediaId = 154587,
            title = "The Dangers in My Heart",
            coverUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800&auto=format&fit=crop&q=80",
            episode = 14,
            dayOfWeek = 7, // Sunday
            airTime = "01:30 JST",
            countdownSeconds = 98400L,
            aniListScore = 8.7f
        ),
        ReleaseScheduleItem(
            mediaId = 140960,
            title = "SPY x FAMILY Episode Special",
            coverUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800&auto=format&fit=crop&q=80",
            episode = 26,
            dayOfWeek = 6, // Saturday
            airTime = "23:00 JST",
            countdownSeconds = 45000L,
            aniListScore = 8.3f
        )
    )

    val sampleActivities: List<ActivityItem> = listOf(
        ActivityItem(
            id = "act_1",
            userName = "Kaito_Sora",
            userAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80",
            activityType = "watched",
            progressText = "Watched episode 18 of 28",
            text = "The spell casting choreo in Frieren episode 18 blew my mind. Pure cinema by Madhouse!",
            timeAgo = "12m ago",
            likesCount = 89,
            mediaId = 153518,
            mediaTitle = "Frieren: Beyond Journey's End",
            mediaCover = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&auto=format&fit=crop&q=80",
            score = 10f
        ),
        ActivityItem(
            id = "act_2",
            userName = "Aiko_Natsuki",
            userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
            activityType = "completed",
            progressText = "Completed 28 of 28",
            text = "Just finished rewatching Attack on Titan with my brother. We both shed tears at the end credit scene.",
            timeAgo = "45m ago",
            likesCount = 214,
            mediaId = 16498,
            mediaTitle = "Attack on Titan: The Final Chapters",
            mediaCover = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=400&auto=format&fit=crop&q=80",
            score = 10f
        ),
        ActivityItem(
            id = "act_3",
            userName = "Ren_Miyamoto",
            userAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200&auto=format&fit=crop&q=80",
            activityType = "reviewed",
            progressText = "Rated 9.5 / 10",
            text = "Solo Leveling Arise has established itself as one of the smoothest manhwa adaptations of the decade.",
            timeAgo = "2h ago",
            likesCount = 142,
            mediaId = 151807,
            mediaTitle = "Solo Leveling: Arise",
            mediaCover = "https://images.unsplash.com/photo-1563089145-599997674d42?w=400&auto=format&fit=crop&q=80",
            score = 9.5f
        ),
        ActivityItem(
            id = "act_4",
            userName = "Yuki_Sakura",
            userAvatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=200&auto=format&fit=crop&q=80",
            activityType = "rated",
            progressText = "Rated 9.0 / 10",
            text = "The Dangers in My Heart Season 2 is the gold standard for romantic pacing and emotional payoff.",
            timeAgo = "4h ago",
            likesCount = 76,
            mediaId = 154587,
            mediaTitle = "The Dangers in My Heart S2",
            mediaCover = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=400&auto=format&fit=crop&q=80",
            score = 9.0f
        )
    )

    val sampleStreamSources: List<StreamSource> = listOf(
        StreamSource(
            id = "src_1080p",
            quality = "1080p Ultra",
            resolutionLabel = "1920x1080 • 60 FPS",
            // High quality reliable test HLS stream
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isHls = false,
            bitrate = "6.2 Mbps",
            latencyMs = 18
        ),
        StreamSource(
            id = "src_720p",
            quality = "720p HD",
            resolutionLabel = "1280x720 • 30 FPS",
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isHls = false,
            bitrate = "3.1 Mbps",
            latencyMs = 24
        ),
        StreamSource(
            id = "src_auto",
            quality = "Auto Adaptive",
            resolutionLabel = "Adaptive HLS (1080p Max)",
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isHls = false,
            bitrate = "4.5 Mbps",
            latencyMs = 15
        )
    )

    val sampleSubtitles: List<SubtitleTrack> = listOf(
        SubtitleTrack("sub_en", "English [Full Subtitles]", "ASS", isSelected = true),
        SubtitleTrack("sub_es", "Español Latinoamericano", "SRT", isSelected = false),
        SubtitleTrack("sub_fr", "Français", "SRT", isSelected = false),
        SubtitleTrack("sub_de", "Deutsch", "SRT", isSelected = false),
        SubtitleTrack("sub_off", "Off (Audio Only)", "NONE", isSelected = false)
    )
}
