package com.example.common.compoment


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.R
import com.example.data.models.NotesData
import com.example.domain.enum.SelectLocationStatus

@Composable
fun ViewLocationItem(
    item: NotesData,
    selectItems: (status: SelectLocationStatus,note: NotesData)-> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row {
            Column(modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    selectItems.invoke(SelectLocationStatus.FIND_LOCATION,item)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.location_img_icon),
                    contentDescription = "Description for accessibility",
                    modifier = Modifier.size(50.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(180.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "User: ${item.userName}",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Row(modifier = Modifier.align(Alignment.Start), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Title: ",style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(text = item.titleNotes)
                }
                Text(text = item.time)
            }
            Column(modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    selectItems.invoke(SelectLocationStatus.SHOW_NOTE,item)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.note_img_icon),
                    contentDescription = "Description for accessibility",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            if (item.userEdit) {
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Image(
                        painter = painterResource(id = R.drawable.edit_img_icon),
                        contentDescription = "edit",
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { selectItems.invoke(SelectLocationStatus.EDIT_NOTE,item)}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.delete_img_icon),
                        contentDescription = "delete",
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { selectItems.invoke(SelectLocationStatus.DELETE_NOTE,item)}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ViewLocationItemPreview() {

}