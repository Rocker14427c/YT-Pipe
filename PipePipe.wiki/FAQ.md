# Troubleshooting

## Crash without any bug report

Please download [LogFox](https://f-droid.org/en/packages/com.f0x1d.logfox/), keep it running and retry. You will get a error report so that you can raise an issue with that.
*Your device doesn't have to be rooted.

# Errors

## java.lang.OutOfMemoryError

### OOM-1-240713

<details>
<summary>Pattern</summary>
<code>Failed to allocate a .* okio\.RealBufferedSource\.read\(RealBufferedSource\.kt:39\).*okhttp3\.internal\.http2\.Http2Stream\$FramingSource\.receive\$okhttp\(Http2Stream\.kt:445\)</code>
</details>

Memory leak caused by some bad designed legacy code. See https://github.com/TeamNewPipe/NewPipe/issues/10496.
Rewriting such codes, especially the feed fetcher, is a big work and not planned at this time. It is recommended not to depend on the feeds.

## java.lang.RuntimeException

### RE-1-240713

<details>
<summary>Pattern</summary>
<code>Dummy</code>
</details>

For debugging purpose. Ignore it.

## java.lang.IllegalArgumentException

### IAE-1-240713

<details>
<summary>Pattern</summary>
<code>Comparison method violates its general contract!</code>
</details>

Please download the latest version.

### IAE-2-240713

<details>
<summary>Pattern</summary>
<code>Player is accessed on the wrong thread.</code>
</details>

Please download the latest version.

## java.lang.NullPointerException

### NPE-1-240713

<details>
<summary>Pattern</summary>
<code>PermissionHelper.showPopupEnablementToast</code>
</details>

Please grant the app 'Display over other apps' permission.

## com.google.android.exoplayer2.ExoPlaybackException

### EXO-1-240801

<details>
<summary>Pattern</summary>
<code>MediaCodecVideoRenderer error</code>
</details>

Try to enable some preference:

Settings -> Video & Audio-> ExoSettings

'Use Exoplayer's decoder fallback feature' / 'Always use ExoPlayer's video output surface setting workaround' ,


If it doesn't work, unfortunately your device don't have the ability to decode the video in-app. 

You can still watch the video by opening it in external players (like VLC)

### EXO-2-240725

<details>
<summary>Pattern</summary>
<code>InvalidResponseCodeException: Response code: 403</code>
</details>

Please update to the latest version (>= v3.6.1)

## com.google.android.exoplayer2.PlaybackException

### PE-1-240725

<details>
<summary>Pattern</summary>
<code>org.schabi.newpipe.player.Player.onBroadcastReceived</code>
</details>

3.6.0 or lower:
Please update to the latest version (>= v3.6.1)

3.6.1 or higher:
The stream currently playing is buffering for 10+ second and PipePipe decides to stop it. This usually happens because the service you are using is in an unstable state.

## org.schabi.newpipe.extractor.exceptions.ParsingException

### PE-B-1-240713

<details>
<summary>Pattern</summary>
<code>风控校验失败</code>
</details>

BiliBili has a strict risk control mechanism. The anonymous method we use does not transmit any personal information, so it may be intercepted probabilistically. If this error occurs when accessing an individual channel page, it can usually be resolved by retrying. Another situation is trying to retrieve updates for all the channels you follow - this will lead to a large number of requests, and almost 100% will be detected by the risk control system and block your IP. Once blocked, it usually takes a few hours to recover, during which time you will not be able to access the channels, and it may also affect other functions such as search. If you often fetch channel updates, **AVOID** following more than 10 BiliBili channels in your channel group. If possible, it's best to keep it to under 5 to ensure stability.

We plan to enable an optional update method for logged-in accounts in the future, which might avoid this issue, but it will take time.

## org.schabi.newpipe.extractor.exceptions.ContentNotAvailableException

### CNAE-Y-1-240801

<details>
<summary>Pattern</summary>
<code>Sign in to confirm that you're not a bot</code>
</details>

Your IP address has been temporarily banned by YouTube, please try again later.
