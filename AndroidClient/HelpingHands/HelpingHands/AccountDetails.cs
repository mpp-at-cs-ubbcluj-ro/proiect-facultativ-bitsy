using Android.Content;
using Android.Views;
using Android.Widget;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.UI;
using HelpingHands.Utils;
using System;
using System.Linq;
using System.Threading.Tasks;

namespace HelpingHands
{
    public partial class MainVoluntarActivity
    {
        [Control] GridLayout ProfileView;

        [Control] TextView AccPrenume;
        [Control] TextView AccNume;
        [Control] TextView AccEmail;
        [Control] TextView AccXpPct;
        [Control] TextView AccInterests;
        [Control] Button AccApplyForSponsorButton;

        protected void OnCreate_AccountPage()
        {            
            AccApplyForSponsorButton.Click += ApplyForSponsorButton_Click;

            ProfileView.Visibility = ViewStates.Gone;
            Task.Run(Load);
        }

        async void Load()
        {
            try
            {
                RunOnUiThread(async () =>
                {
                    Console.WriteLine("Account Profile Details");
                    AccNume.Text =AppSession.UserData.User.Nume;
                    AccEmail.Text = AppSession.UserData.User.Email;
                    AccPrenume.Text = AppSession.UserData.User.Prenume;
                    AccXpPct.Text = AppSession.UserData.User.XpPoints.ToString();
                    var interestsList = await API.Client.GetVoluntarInterests(AppSession.UserId);
                    var interestsText = string.Join(", ", interestsList.Select(x => x.Name));

                    AccInterests.Text = interestsText;
                });
            }
            catch (Exception e)
            {
                await MessageBox.Alert(this, $"FAILED ACCOUNT PAGE\n\n{e.Message}", "Error");
            }
        }

        private void ApplyForSponsorButton_Click(object sender, EventArgs e)
        {
            Intent intent = new Intent(this, typeof(ApplySponsorshipActivity));
            StartActivity(intent);
        }
    }
}