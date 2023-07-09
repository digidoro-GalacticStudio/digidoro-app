package com.galacticstudio.digidoro.ui.screens.onboarding

import android.content.res.Configuration
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.galacticstudio.digidoro.R
import com.galacticstudio.digidoro.data.OnBoardingData
import com.galacticstudio.digidoro.ui.theme.AzureBlue10
import com.galacticstudio.digidoro.ui.theme.DigidoroTheme
import com.galacticstudio.digidoro.ui.theme.Gray30
import com.galacticstudio.digidoro.ui.theme.Gray60
import com.galacticstudio.digidoro.ui.theme.HeadlineSmall
import com.galacticstudio.digidoro.ui.theme.White60

@OptIn(ExperimentalFoundationApi::class)
@Preview(name = "Full Preview", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)

@Composable
fun OnBoardingScreenPreview() {
    DigidoroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainFunction()
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun PreviewFunction(){
    Surface(modifier = Modifier.fillMaxSize()) {
        MainFunction()
    }
}

//TODO: Deje en el Dummy Text, por si queres pasarlo ahi y dejar de tener aqui la data sisisis
@ExperimentalFoundationApi
@Composable
fun MainFunction(){
    val items = ArrayList<OnBoardingData>()

    items.add(
        OnBoardingData(
            R.drawable.welcome,
            "Welcome to the digi community!",
            "We're thrilled to have you on board and help you supercharge your productivity."
        )
    )

    items.add(
        OnBoardingData(
            R.drawable.hero_pic,
            "Your all in one app",
            "By combining task management, note-taking, and the Pomodoro technique, we provide a comprehensive solution to boost your efficiency and help you stay on top of your goals."
        )
    )

    items.add(
        OnBoardingData(
            R.drawable.ranking_landing,
            "Stay motivated and challenge yourself",
            "You can track your own level and progress, as well as compare your achievements with those of your friends!"
        )
    )

    val pagerState = rememberPagerState(
        pageCount = items.size,

        infiniteLoop = false,
        initialPage = 0
    )

    OnBoardingPager(item = items,
        pagerState = pagerState,
        modifier = Modifier.fillMaxWidth().background(color = White60)
    )
}


@ExperimentalFoundationApi
@Composable
    fun OnBoardingPager(
        item:List<OnBoardingData>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(state = pagerState, pageCount = 3){ page ->
                    Column(modifier = Modifier
                        .padding(top = 60.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Image(painter = painterResource(id = item[page].image), contentDescription = item[page].tittle,                 modifier = Modifier.height(250.dp).fillMaxWidth()
                        )
                        Text(
                            text =item[page].tittle,
                            modifier = Modifier.padding(top = 50.dp),
                            color = Gray30,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = item[page].desc,
                            modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
                            color = Gray30,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                PagerIndicator(item.size, pagerState.currentPage)
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)){
                BottomSection(pagerState.currentPage)
            }
        }
    }

@ExperimentalFoundationApi
@Composable
//TODO: aqui hay que cambiar el pager: https://developer.android.com/jetpack/compose/layouts/pager
    fun rememberPagerState(
    @IntRange(from = 0) pageCount: Int,
    @IntRange(from = 0) initialPage: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @IntRange(from = 1) initialOffscreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {
    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffscreenLimit,
        infiniteLoop = infiniteLoop
    )
}

@ExperimentalFoundationApi
@Composable
fun PagerIndicator(
    size: Int,
    currentPage: Int,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 60.dp)
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
        }
    }
}

/*{
    repeat(size) { val color = if (PagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
        Box(
            modifier = Modifier
                .padding(2.dp)
                .clip(CircleShape)
                .background(color)
                .size(20.dp)
        )
    }

}*/

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = if (isSelected) 25.dp else 10.dp)
    val color = if (isSelected) Color.DarkGray else Color.LightGray

    Box(
        modifier = Modifier.padding(1.dp).height(10.dp).width(width.value).clip(CircleShape).background(color)

    )
}

@ExperimentalFoundationApi
@Composable
fun BottomSection(currentPager: Int) {
    Row(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (currentPager != 2) Arrangement.SpaceBetween else Arrangement.Center
    ) {

        if (currentPager == 2){
            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(50), // = 40% percent
            ) {
                Text(
                    text = "Get Started",
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 40.dp),
                    color = AzureBlue10
                )
            }
        }else{
            SkipNextButton("Skip",Modifier.padding(start = 20.dp))
            SkipNextButton("Next",Modifier.padding(end = 20.dp))
        }

    }
}

@Composable
fun SkipNextButton(text: String, modifier: Modifier) {
    Text(
        text = text, color = Gray60,
        style = HeadlineSmall,
    )

}

