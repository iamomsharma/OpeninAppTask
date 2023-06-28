package com.example.openinapptask.model

data class DashboardModel(
    val applied_campaign: Int,
    val data: Data,
    val extra_income: Double,
    val links_created_today: Int,
    val message: String,
    val startTime: String,
    val status: Boolean,
    val statusCode: Int,
    val support_whatsapp_number: String,
    val today_clicks: Int,
    val top_location: String,
    val top_source: String,
    val total_clicks: Int,
    val total_links: Int,
    var isTopLinkCheck: Boolean = true,
    var isRecentLinkCheck: Boolean = false
) {
    data class Data(
        val recent_links: List<RecentLink>,
        val top_links: List<TopLink>,
        val overall_url_chart: Map<String, Int>
    ) {

        data class RecentLink(
            val app: String,
            val created_at: String,
            val domain_id: String,
            val original_image: String,
            val smart_link: String,
            val thumbnail: Any,
            val times_ago: String,
            val title: String,
            val total_clicks: Int,
            val url_id: Int,
            val url_prefix: Any,
            val url_suffix: String,
            val web_link: String
        )

        data class TopLink(
            val app: String,
            val created_at: String,
            val domain_id: String,
            val original_image: String,
            val smart_link: String,
            val thumbnail: Any,
            val times_ago: String,
            val title: String,
            val total_clicks: Int,
            val url_id: Int,
            val url_prefix: String,
            val url_suffix: String,
            val web_link: String
        )
    }
}
