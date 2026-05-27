# SchoolViewer

SchoolViewer は、中学受験における「英語入試・帰国生入試」情報を Android 端末から閲覧するためのアプリです。

Web アプリ版と同じ Helidon バックエンド API に接続し、学校情報の一覧表示、検索、詳細確認を Android アプリ上で行えるようにしています。

## 概要

このアプリは、`helidon-school-app` の学校データを Android 端末から閲覧するためのクライアントアプリです。

Web ブラウザからのアクセスだけでなく、Android スマートフォンや Android エミュレータからも学校情報を確認できます。

主な用途は以下です。

- 中学受験における英語入試情報の確認
- 帰国生入試・英語入試を実施する学校の検索
- 学校ごとの試験日、定員、科目などの確認
- Web アプリと同じデータソースを使った Android 向け表示

## 主な機能

- 学校情報の一覧表示
- 学校名による検索
- 学校詳細画面の表示
- 最新データの再読み込み
- Helidon バックエンド API との連携
- Jetpack Compose による Android UI

## システム構成

```text
Android App
  |
  | Retrofit
  v
Helidon Backend API
  |
  v
School Data
```

Android アプリは、以下のバックエンド API に接続します。

```text
https://helidon-school-app.onrender.com/
```

学校情報の取得には、主に以下の API を使用します。

```text
GET /api/schools
GET /api/schools?name={schoolName}
```

## 技術スタック

- Kotlin
- Android
- Jetpack Compose
- Material 3
- Android Navigation Compose
- ViewModel
- Kotlin Coroutines
- Retrofit
- Gson Converter
- Gradle Kotlin DSL

## Android 要件

- minSdk: 26
- targetSdk: 35
- compileSdk: 35
- Java / Kotlin JVM target: 17

## ディレクトリ構成

```text
SchoolViewer/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/com/naiveprince/schoolviewer/
│       │       ├── MainActivity.kt
│       │       ├── data/
│       │       ├── model/
│       │       ├── network/
│       │       └── ui/
│       ├── androidTest/
│       └── test/
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

## 主要ファイル

### `MainActivity.kt`

アプリのエントリーポイントです。

Jetpack Compose を使って、学校一覧画面、検索欄、詳細画面への遷移、更新ボタンなどを構成しています。

### `network/ApiClient.kt`

Retrofit の設定を行うファイルです。

バックエンド API のベース URL を定義し、`SchoolApi` を生成します。

### `network/SchoolApi.kt`

学校情報を取得する API インターフェースです。

主に以下の API に対応しています。

```text
GET /api/schools
GET /api/schools?name={name}
```

## ビルド方法

Android Studio でこのリポジトリを開きます。

```bash
git clone https://github.com/naiveprince/SchoolViewer.git
cd SchoolViewer
```

Android Studio でプロジェクトを開いた後、Gradle Sync を実行してください。

その後、以下のいずれかで起動できます。

- Android Studio の Run ボタンから実行
- Android Emulator で実行
- 実機 Android 端末で実行

## コマンドラインでのビルド

Debug APK を作成する場合:

```bash
./gradlew :app:assembleDebug
```

Release APK を作成する場合:

```bash
./gradlew :app:assembleRelease
```

## API 接続先の変更

API 接続先は以下のファイルで設定されています。

```text
app/src/main/java/com/naiveprince/schoolviewer/network/ApiClient.kt
```

ローカル開発環境の API に接続する場合は、例として以下のように変更できます。

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```

Android Emulator からホスト PC 上のローカルサーバーへ接続する場合、`localhost` ではなく `10.0.2.2` を使用します。

本番環境では以下のような URL を使用します。

```kotlin
private const val BASE_URL = "https://helidon-school-app.onrender.com/"
```

## Web アプリ版との関係

この Android アプリは、Web アプリ版と同じ学校データを利用する Android クライアントです。

```text
Web App
Android App
   |
   v
Common Helidon Backend API
```

そのため、バックエンド側の学校データが更新されると、Android アプリ側でも最新データを取得できます。

## 注意事項

- このアプリはバックエンド API へのネットワーク接続を必要とします。
- Render 上の無料プラン等を利用している場合、初回アクセス時に API の起動に時間がかかることがあります。
- API の URL を変更した場合は、アプリを再ビルドしてください。
- 公開リポジトリに署名鍵、パスワード、API トークンなどの機密情報を含めないでください。

## 今後の改善案

- 学校種別・地域・試験区分による絞り込み
- お気に入り登録
- オフラインキャッシュ
- 詳細画面の情報表示改善
- Web アプリ版との UI 統一
- GitHub Actions による APK 自動ビルド

## Repository

```text
SchoolViewer
```

Android app module:

```text
app/
```

## License

This project is provided for demonstration and educational purposes.
