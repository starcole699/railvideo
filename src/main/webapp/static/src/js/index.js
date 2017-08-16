$(function () {
  var render = Handlebars.getTemplate('table');
  var html = render({list: [{name: 'A', value: 1}, {name: 'B', value: 2}]});
  $('#main').html(html);

  var myChart = Highcharts.chart('chart', {
    chart: {
      type: 'bar'
    },
    title: {
      text: 'Fruit Consumption'
    },
    xAxis: {
      categories: ['Apples', 'Bananas', 'Oranges']
    },
    yAxis: {
      title: {
        text: 'Fruit eaten'
      }
    },
    series: [{
      name: 'Jane',
      data: [1, 0, 4]
    }, {
      name: 'John',
      data: [5, 7, 3]
    }]
  });
});