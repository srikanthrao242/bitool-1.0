package com.bitool.analytics.metrics

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.hotspot.DefaultExports

/**
  * Akka Http service providing metrics info for monitoring and alerts via prometheus
  *
  * @author kcpaul
  */
class MetricsService {

  DefaultExports.initialize()

  val routes: Route = {
    get {
      path("metrics") {
        complete {
          MetricFamilySamplesEntity.fromRegistry(CollectorRegistry.defaultRegistry)
        }
      }
    }
  }
}
