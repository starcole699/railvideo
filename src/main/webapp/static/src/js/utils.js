$(function () {
  Handlebars.getTemplate = function (name) {
    if (Handlebars.templates === undefined || Handlebars.templates[name] === undefined) {
      $.ajax({
        url: 'src/templates/' + name + '.html',
        success: function (data) {
          if (Handlebars.templates === undefined) {
            Handlebars.templates = {};
          }
          Handlebars.templates[name] = Handlebars.compile(data);
        },
        async: false
      });
    }
    return Handlebars.templates[name];
  };
});