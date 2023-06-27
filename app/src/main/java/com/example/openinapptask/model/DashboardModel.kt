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
        val overall_url_chart: OverallUrlChart,
        val recent_links: List<RecentLink>,
        val top_links: List<TopLink>
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

        data class OverallUrlChart(
            val `2023-05-22`: Int,
            val `2023-05-23`: Int,
            val `2023-05-24`: Int,
            val `2023-05-25`: Int,
            val `2023-05-26`: Int,
            val `2023-05-27`: Int,
            val `2023-05-28`: Int,
            val `2023-05-29`: Int,
            val `2023-05-30`: Int,
            val `2023-05-31`: Int,
            val `2023-06-01`: Int,
            val `2023-06-02`: Int,
            val `2023-06-03`: Int,
            val `2023-06-04`: Int,
            val `2023-06-05`: Int,
            val `2023-06-06`: Int,
            val `2023-06-07`: Int,
            val `2023-06-08`: Int,
            val `2023-06-09`: Int,
            val `2023-06-10`: Int,
            val `2023-06-11`: Int,
            val `2023-06-12`: Int,
            val `2023-06-13`: Int,
            val `2023-06-14`: Int,
            val `2023-06-15`: Int,
            val `2023-06-16`: Int,
            val `2023-06-17`: Int,
            val `2023-06-18`: Int,
            val `2023-06-19`: Int,
            val `2023-06-20`: Int,
            val `2023-06-21`: Int
        )

    }
}
