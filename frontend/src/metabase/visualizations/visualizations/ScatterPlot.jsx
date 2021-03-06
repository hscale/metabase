/* @flow */

import { t } from "ttag";
import LineAreaBarChart from "../components/LineAreaBarChart.jsx";
import { scatterRenderer } from "../lib/LineAreaBarRenderer";

import {
  GRAPH_DATA_SETTINGS,
  GRAPH_BUBBLE_SETTINGS,
  GRAPH_GOAL_SETTINGS,
  GRAPH_COLORS_SETTINGS,
  GRAPH_AXIS_SETTINGS,
} from "../lib/settings/graph";
import { PLUGIN_CHART_SETTINGS } from "metabase/plugins";

export default class ScatterPlot extends LineAreaBarChart {
  static uiName = t`Scatter`;
  static identifier = "scatter";
  static iconName = "bubble";
  static noun = t`scatter plot`;

  static renderer = scatterRenderer;

  static settings = {
    ...GRAPH_DATA_SETTINGS,
    ...GRAPH_BUBBLE_SETTINGS,
    ...GRAPH_GOAL_SETTINGS,
    ...GRAPH_COLORS_SETTINGS,
    ...GRAPH_AXIS_SETTINGS,
    ...PLUGIN_CHART_SETTINGS,
  };
}
