package org.opentripplanner.apis.gtfs.mapping.routerequest;

import static org.opentripplanner.utils.collection.CollectionUtils.requireNullOrNonEmpty;

import org.opentripplanner.apis.gtfs.GraphQLUtils;
import org.opentripplanner.apis.gtfs.generated.GraphQLTypes;
import org.opentripplanner.apis.gtfs.generated.GraphQLTypes.GraphQLTransitFilterSelectInput;
import org.opentripplanner.apis.gtfs.mapping.TransitModeMapper;
import org.opentripplanner.routing.api.request.request.filter.SelectRequest;
import org.opentripplanner.transit.model.basic.MainAndSubMode;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.utils.collection.CollectionUtils;

class SelectRequestMapper {

  static SelectRequest.Builder mapSelectRequest(
    GraphQLTransitFilterSelectInput input,
    String name
  ) {
    var routes = input.getGraphQLRoutes();
    var agencies = input.getGraphQLAgencies();
    var routeShortNames = input.getGraphQLRouteShortNames();

    requireNullOrNonEmpty(routes, "preferences.transit.filters.%s.routes".formatted(name));
    requireNullOrNonEmpty(agencies, "preferences.transit.filters.%s.agencies".formatted(name));
    requireNullOrNonEmpty(routeShortNames, "preferences.transit.filters.%s.routeShortNames".formatted(name));
    if (CollectionUtils.isEmpty(routes) && CollectionUtils.isEmpty(agencies) && CollectionUtils.isEmpty(routeShortNames)) {
      var type = GraphQLUtils.typeName(input);
      throw new IllegalArgumentException(
        "%s must contain at least one element in either 'routes', 'agencies' or 'routeShortNames'.".formatted(type)
      );
    }

    var selectRequestBuilder = SelectRequest.of();

    if (CollectionUtils.hasValue(routes)) {
      selectRequestBuilder.withRoutes(FeedScopedId.parse(routes));
    }

    if (CollectionUtils.hasValue(agencies)) {
      selectRequestBuilder.withAgencies(FeedScopedId.parse(agencies));
    }

    if (CollectionUtils.hasValue(routeShortNames)) {
      selectRequestBuilder.withRouteShortNames(routeShortNames);
    }

    return selectRequestBuilder;
  }
}
