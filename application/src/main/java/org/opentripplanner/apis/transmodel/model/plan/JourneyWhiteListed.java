package org.opentripplanner.apis.transmodel.model.plan;

import static org.opentripplanner.apis.transmodel.support.GqlUtil.newIdListInputField;

import graphql.Scalars;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.opentripplanner.api.model.transit.FeedScopedIdMapper;
import org.opentripplanner.apis.transmodel.support.GqlUtil;
import org.opentripplanner.model.TripTimeOnDate;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.network.Route;
import org.opentripplanner.transit.model.timetable.Trip;

public class JourneyWhiteListed {

  public static final GraphQLInputObjectType INPUT_TYPE = GraphQLInputObjectType.newInputObject()
    .name("InputWhiteListed")
    .description(
      "Filter trips by only allowing lines involving certain " +
      "elements. If both lines and authorities are specified, only one must be valid " +
      "for each line to be used. If a line is both banned and whitelisted, it will " +
      "be counted as banned."
    )
    .field(newIdListInputField("lines", "Set of ids for lines that should be used"))
    .field(newIdListInputField("authorities", "Set of ids for authorities that should be used"))
    .field(
      GqlUtil.newIdListInputField(
        "rentalNetworks",
        "Set of ids of rental networks that should be used for renting vehicles."
      )
    )
    .field(
      GraphQLInputObjectField.newInputObjectField()
        .name("routeShortNames")
        .description("Set of ids of route short names that should be used for filtering routes.")
        .type(new GraphQLList(Scalars.GraphQLString))
        .defaultValue(List.of())
        .build()
    )
    .build();

  public final Set<FeedScopedId> authorityIds;
  public final Set<FeedScopedId> lineIds;
  public final Set<String> routeShortNames;

  public JourneyWhiteListed(DataFetchingEnvironment environment, FeedScopedIdMapper idMapper) {
    Map<String, List<String>> whiteList = environment.getArgument("whiteListed");
    if (whiteList == null) {
      this.authorityIds = Set.of();
      this.lineIds = Set.of();
      this.routeShortNames = Set.of();
    } else {
      this.authorityIds = Set.copyOf(idMapper.parseListNullSafe(whiteList.get("authorities")));
      this.lineIds = Set.copyOf(idMapper.parseListNullSafe(whiteList.get("lines")));
      this.routeShortNames = Set.copyOf(whiteList.getOrDefault("routeShortNames", List.of()));
    }
  }

  public static Stream<TripTimeOnDate> whiteListAuthoritiesAndOrLines(
    Stream<TripTimeOnDate> stream,
    Collection<FeedScopedId> authorityIds,
    Collection<FeedScopedId> lineIds,
    Collection<String> routeShortNames
  ) {
    if (authorityIds.isEmpty() && lineIds.isEmpty() && routeShortNames.isEmpty()) {
      return stream;
    }
    return stream.filter(it ->
      isTripTimeOnDateAcceptable(it, authorityIds, lineIds, routeShortNames)
    );
  }

  private static boolean isTripTimeOnDateAcceptable(
    TripTimeOnDate tts,
    Collection<FeedScopedId> authorityIds,
    Collection<FeedScopedId> lineIds,
    Collection<String> routeShortNames
  ) {
    Trip trip = tts.getTrip();

    if (trip == null || trip.getRoute() == null) {
      return true;
    }

    Route route = trip.getRoute();

    String actualShortName = route.getShortName();

    if (!routeShortNames.isEmpty() && actualShortName != null) {
      System.out.println(
        "Checking Route: " +
        route.getId() +
        " | Actual ShortName: [" +
        actualShortName +
        "]" +
        " | Searching for: " +
        routeShortNames
      );
    }

    // If no filters are set, accept all
    if (authorityIds.isEmpty() && lineIds.isEmpty() && routeShortNames.isEmpty()) {
      return true;
    }

    // Check each filter and return true if any matches
    if (!authorityIds.isEmpty() && authorityIds.contains(route.getAgency().getId())) {
      return true;
    }

    if (!lineIds.isEmpty() && lineIds.contains(route.getId())) {
      return true;
    }

    if (
      !routeShortNames.isEmpty() &&
      route.getShortName() != null &&
      routeShortNames.contains(route.getShortName())
    ) {
      return true;
    }

    // No filter matched
    return false;
  }
}
