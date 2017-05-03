const loaders = require("./loaders");
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

const devBuild = process.env.NODE_ENV === 'dev';

// Loaders specific to compiling
loaders.push({
    test: /\.tsx?$/,
    enforce: 'pre',
    loader: 'tslint-loader',
    exclude: /node_modules/,
    options: {
        typeCheck: true
    }
});

module.exports = [{
    entry: {
        main: [ path.resolve('./src/client/index.ts') ]
    },
    output: {
        path: path.resolve('dist'),
        filename: '[name].js'
    },
    resolve: {
        extensions: [ '.ts', '.tsx', '.js', '.json' ],
        modules: [ 'src/client', 'src/styles', 'node_modules' ]
    },
    devtool: 'source-map', //devBuild ? 'cheap-eval-source-map' : 'source-map',
    plugins: [
        new HtmlWebpackPlugin({
            template: './src/html/index.html',
            inject: 'body',
            hash: true
        }),
        new webpack.ProvidePlugin({
        })
    ],
    module: {
        loaders: loaders
    }
}];

if (!devBuild) {
    module.exports[0].plugins.push(new webpack.optimize.UglifyJsPlugin({minimize: true}));
}
