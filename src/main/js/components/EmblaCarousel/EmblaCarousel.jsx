import {
  PrevButton,
  NextButton,
  usePrevNextButtons
} from './EmblaCarouselArrowButtons'
import './embla.css'
import useEmblaCarousel from 'embla-carousel-react'

const EmblaCarousel = ({ title, slides, options }) => {
  const [emblaRef, emblaApi] = useEmblaCarousel(options)

  const {
    prevBtnDisabled,
    nextBtnDisabled,
    onPrevButtonClick,
    onNextButtonClick
  } = usePrevNextButtons(emblaApi)

  return (
    <section className="embla">
        <div className='text-2xl font-bold mb-3 text-white'>{title}</div>
        <div className="embla__viewport" ref={emblaRef}>
            <div className="embla__container">
            {slides.map((index) => (
                <a className="embla__slide bg-slate-400/10 rounded-lg" key={index} href="#">
                    <div className="embla__slide__number">{index + 1}</div>
                </a>
            ))}
            </div>
        </div>

        <div className="embla__controls">
            <div className="embla__buttons">
            <PrevButton onClick={onPrevButtonClick} disabled={prevBtnDisabled} />
            <NextButton onClick={onNextButtonClick} disabled={nextBtnDisabled} />
            </div>
        </div>
    </section>
  );
}

export default EmblaCarousel;
