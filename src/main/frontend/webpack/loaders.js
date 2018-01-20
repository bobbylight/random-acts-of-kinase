module.exports = [
    {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        loader: 'ts-loader',
        options: {
            // Needed for <script lang="ts"> to work in *.vue files; see https://github.com/vuejs/vue-loader/issues/109
            appendTsSuffixTo: [ /\.vue$/ ]
        }
    }, {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: {
            preLoaders: {
                //ts: 'tslint-loader?typeCheck'
                ts: 'tslint-loader'
            },
            loaders: {
                ts: 'ts-loader',
                css: 'vue-style-loader!css-loader' // <style lang="css">
            }
        }
    }, {
        test: /\.css$/,
        loader: 'style-loader!css-loader'
    }, {
        test: /\.scss$/,
        loader: 'style-loader!css-loader!sass-loader'
    }, {
        test: /\.less$/,
        loader: 'style-loader!css-loader!less-loader'
    }, {
        test: /\.html$/,
        exclude: /node_modules/,
        loader: 'raw-loader'
    }, {
        test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'url-loader?limit=10000&mimetype=application/font-woff'
    }, {
        test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'file-loader'
    }, {
        test: /\.jpg$/,
        exclude: /node_modules/,
        loader: 'file-loader'
    }, {
        test: /\.png$/,
        exclude: /node_modules/,
        loader: 'url-loader'
    }
];
