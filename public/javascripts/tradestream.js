
var tradeStream = (function() {
    var geoChart;
    var currencyChart;
    var updateTrendChart;

    var initCharts = function() {
        drawRegionsMap();
        drawCurrencyChart();
        drawTrendChart();
    };

    var drawTrendChart = function() {
        var chart = new SmoothieChart(
                    {millisPerPixel:50,
                    grid:{fillStyle:'#ffffff',strokeStyle:'transparent',millisPerLine:10000,
                        verticalSections:0,borderVisible:false},
                    labels:{fillStyle:'rgba(0,0,0,0.67)',fontSize:15,precision:0},
                    timestampFormatter:SmoothieChart.timeFormatter}),
            canvas = document.getElementById('trend_chart');
            eurToUsdSeries = new TimeSeries();
            usdToEurSeries = new TimeSeries();

        chart.addTimeSeries(eurToUsdSeries, {lineWidth:3,strokeStyle:'#00ff00'});
        chart.addTimeSeries(usdToEurSeries, {lineWidth:3,strokeStyle:'#0024ff'});
        chart.streamTo(canvas, 1000);

        tradeStream.updateTrendChart = function(eurToUsd, usdToEur, timestamp) {
            eurToUsdSeries.append(new Date().getTime(), eurToUsd);
            usdToEurSeries.append(new Date().getTime(), usdToEur);
        }
    };

    var drawCurrencyChart = function() {
        var data = google.visualization.arrayToDataTable([
            ['Currency', 'Total']
        ]);

        var options = {};
        tradeStream.currencyChart = new google.visualization.PieChart(document.getElementById('currency_chart'));
        tradeStream.currencyChart.draw(data, options);
    };

    var updateCurrencyChart = function(data) {
        if (typeof tradeStream.currencyChart != "undefined") {
            var options = {};
            tradeStream.currencyChart.draw(new google.visualization.DataTable(data), options);
        }
    };

    var drawRegionsMap = function() {
        var datajs = new google.visualization.DataTable({
            cols: [
                {id: 'A', label: 'Country', type: 'string'},
                {id: 'B', label: 'Trades', type: 'number'}
            ],
            rows: [
            ]
        });

        var options = {};
        tradeStream.geoChart = new google.visualization.GeoChart(document.getElementById('regions_chart'));
        tradeStream.geoChart.draw(datajs, options);
    };

    var updateRegionsMap = function(data) {
        if (typeof tradeStream.geoChart != "undefined") {
            var options = {};
            tradeStream.geoChart.draw(new google.visualization.DataTable(data), options);
        }
    };

    var updateDash = function(msg) {
        var parsedMsg = JSON.parse(msg.data);
        if ("geochart" in parsedMsg) {
            updateRegionsMap(parsedMsg["geochart"]);
        }
        if ("currencychart" in parsedMsg) {
            updateCurrencyChart(parsedMsg["currencychart"]);
        }
        if ("trendchart" in parsedMsg) {
            tradeStream.updateTrendChart(
                parsedMsg["trendchart"]["eurToUsd"],
                parsedMsg["trendchart"]["usdToEur"],
                parsedMsg["trendchart"]["timestamp"]
            );
        }
    };

    return {
        updateDash: updateDash,
        initCharts: initCharts
    };
})();

google.load("visualization", "1", {packages:['corechart', 'line','geochart']});
google.setOnLoadCallback(tradeStream.initCharts);

$(document).ready(function () {
    var loc = window.location, ws_uri;
    if (loc.protocol === "https:") {
        ws_uri = "wss:";
    } else {
        ws_uri = "ws:";
    }
    ws_uri += "//" + loc.host;
    ws_uri += loc.pathname + "tradestream";

    var ws = new WebSocket(ws_uri);
    ws.onmessage = function(msg) { tradeStream.updateDash(msg); };
    ws.onopen = function() { ws.send("start"); };
});
