var sage = angular.module('sage', [
  'ngRoute',
  'ngTable'
]);

sage.model = core.model;
sage.repo = core.repo;

//This method's callback is passed to stomp and executed on both successfull connection, as well as disconnect.
setUpApp(function (connected) {

  //Indicates at app start if the app has successfully conenected to the service
  appConfig.connected = connected;

  sage.constant('appConfig', appConfig);
  sage.constant('api', apiMapping);

  angular.element(document).ready(function () {
    try {
      // If the app is already bootstrapped then an error will be thrown
      // caution: if module is not found app will result in blank page with no stack trace!!!
      angular.bootstrap(document, ['core', 'sage', 'ngMessages', 'ui.bootstrap', 'ngFileUpload', 'ui.openseadragon']);
    } catch (e) {
      console.error(e);
      /*
       * If websockets dissconnect the app will attempt to re-bootstrap. Since the app is already running we will
       * end up in this block, and can generate an error indicating the disconnect.
       */
      var doc = angular.element(document);
      var injector = doc.injector();
      if (typeof injector != 'undefined') {
        AlertService = injector.get('AlertService');
        AlertService.add({
          type: "ERROR",
          message: "Web service cannot be reached."
        }, "/app/errors");
      }
    }
  });

});
