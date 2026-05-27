package com.yonishik.schoolviewer.ui
import com.yonishik.schoolviewer.ui.SchoolDetailScreen
import com.yonishik.schoolviewer.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//  import com.yonishik.schoolviewer.R
import com.yonishik.schoolviewer.model.SchoolV2Dto

@Composable
fun SchoolDetailScreen(school: SchoolV2Dto?) {
    if (school == null) {
        Box(modifier = Modifier.fillMaxSize()) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color(0xD0FFFFFF),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "School not found",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        return
    }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = Color(0xCCFFFFFF),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = school.schoolName ?: "(no name)",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2A1E)
                    )

                    school.category?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF405240)
                        )
                    }
                }
            }

            DetailCard("定員", school.capacity?.toString())
            // DetailCard("定員", school.capacity)
            DetailCard("試験日", school.examDates)
            DetailCard("試験科目", school.subjects)
            DetailCard("試験科目特記", school.alternateSubjects)
            DetailCard("面接有無", school.interview)
            DetailCard("英語資格優遇", school.englishQualificationBenefit)
            DetailCard("備考", school.notes)

            LinkCard(
                label = "リンク",
                url = school.infoLink,
                onClick = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
private fun DetailCard(label: String, value: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xEAF7F7F2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF405240),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = value?.takeIf { it.isNotBlank() } ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1E2A1E)
            )
        }
    }
}

@Composable
private fun LinkCard(
    label: String,
    url: String?,
    onClick: (String) -> Unit
) {
    val validUrl = url?.trim()?.takeIf { it.isNotEmpty() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (validUrl != null) {
                    Modifier.clickable { onClick(validUrl) }
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xEAF7F7F2)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF405240),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = validUrl ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                color = if (validUrl != null) Color(0xFF2E5BBA) else Color(0xFF1E2A1E)
            )

            if (validUrl != null) {
                Text(
                    text = "タップしてブラウザで開く",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2E5BBA)
                )
            }
        }
    }
}
