using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Views.InputMethods;
using Android.Widget;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Data;
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Android.Icu.Text.Transliterator;


namespace HelpingHands
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    public class ApplySponsorshipActivity : Activity
    {
        EditText CifFirmaBox;
        EditText TelefonBox;
        EditText AdresaBox;
        EditText NumeBox;
        Spinner TypesBox;
        Button ApplySponsorshipButton;

        SponsorTypesAdapter SponsorTypesAdapter;


        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_apply_sponsorship);

            NumeBox = FindViewById<EditText>(Resource.Id.NumeBox);
            CifFirmaBox = FindViewById<EditText>(Resource.Id.CifFirmaBox);
            AdresaBox = FindViewById<EditText>(Resource.Id.AdresaBox);
            TelefonBox = FindViewById<EditText>(Resource.Id.TelefonBox);

            TypesBox = FindViewById<Spinner>(Resource.Id.TypesBox);

            ApplySponsorshipButton = FindViewById<Button>(Resource.Id.ApplySponsorshipButton);

            ApplySponsorshipButton.Click += ApplySponsorshipButton_Click;

            Task.Run(Load);
        }

        private async void ApplySponsorshipButton_Click(object sender, EventArgs e)
        {
            var volId = AppSession.UserId;
            var cif = CifFirmaBox.Text;
            var telefon = TelefonBox.Text;
            var adresa = AdresaBox.Text;
            var nume = NumeBox.Text;
            var sponsorType = (TypesBox.Adapter as SponsorTypesAdapter)[TypesBox.SelectedItemPosition];

            var cerere = new CerereSponsor
            {
                VolId = volId,
                CifFirma = cif,
                Telefon = telefon,
                Adresa = adresa,
                NumeFirma = nume,
                SponsorType = sponsorType.Name
            };

            try
            {
                await Client.ApplySponsorship(cerere);
                await MessageBox.Alert(this, "Cererea a fost trimisa cu succes");

                var intent = new Intent(this, typeof(MainVoluntarActivity));
                intent.PutExtra("tab", 1);
                StartActivity(intent);
            }
            catch (Exception ex)
            {
                await MessageBox.Alert(this, ex.Message);
            }

        }


        private async void Load()
        {
            SponsorTypesAdapter = new SponsorTypesAdapter(this, (await API.Client.GetSponsorTypes()).ToList());
            RunOnUiThread(() => TypesBox.Adapter = SponsorTypesAdapter);
        }
    }
}