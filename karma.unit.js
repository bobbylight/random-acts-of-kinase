const webpackConfig = require('./webpack/webpack.test');

module.exports = function (config) {
    config.set({
        basePath: '',
        frameworks: [ 'jasmine' ],
        files: [
            'src/app/**/*.spec.ts'
        ],
        exclude: [
        ],
        preprocessors: {
            'src/app/**/*.spec.ts': [ 'webpack' ]
        },
        webpack: {
            module: webpackConfig.module,
            resolve: webpackConfig.resolve
        },
        reporters: [ 'progress' ],
        port: 9876,
        colors: true,
        logLevel: config.LOG_INFO,
        autoWatch: true,
        browsers: [ 'PhantomJS' ],
        singleRun: true,
        concurrency: Infinity
    });
};
