



$(function () {
  var render = Handlebars.getTemplate('table');

  setInterval(function () {
    $.get('/sensors/stats', function (data) {

      var list = [];
      for (var i in data) {
        var name = i.match(/sensorName='([\w]+)'/).pop();
        var type = i.match(/sensorType='([\w]+)'/).pop();
        var channel = i.match(/channel='([\w]+)'/).pop();

        var last = data[i][data[i].length - 1];
        last.startTime = new Date(last.startTime * 1000);

        var obj = {name:name, type:type, channel:channel, data:last};
        list.push(obj);
      }
      console.log(list);

      $('#main').html(render({list: list}));
    })
  }, 1000);


  // var myChart = Highcharts.chart('chart', {
  //   chart: {
  //     type: 'bar'
  //   },
  //   title: {
  //     text: 'Fruit Consumption'
  //   },
  //   xAxis: {
  //     categories: ['Apples', 'Bananas', 'Oranges']
  //   },
  //   yAxis: {
  //     title: {
  //       text: 'Fruit eaten'
  //     }
  //   },
  //   series: [{
  //     name: 'Jane',
  //     data: [1, 0, 4]
  //   }, {
  //     name: 'John',
  //     data: [5, 7, 3]
  //   }]
  // });
});

