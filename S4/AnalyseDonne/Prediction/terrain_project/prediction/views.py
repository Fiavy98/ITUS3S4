from django.shortcuts import render

# Create your views here.
def home(request):
    return render(request, 'index.html')


def predict(request):
    lat = request.GET.get('lat')
    lon = request.GET.get('lon')

    # TEST : prix fictif
    prix = 100000  # Ar/m²

    return JsonResponse({
        'prix': prix,
        'lat': lat,
        'lon': lon
    })